package net.tarilabs.joind_ex042015;

import static java.util.stream.Collectors.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FetchData {
	static final Logger LOG = LoggerFactory.getLogger(FetchData.class);
	private static String dataPathPrefix ;
	static Client client = ClientBuilder.newBuilder().build();
	
    public static void main( String[] args ) throws JsonProcessingException, IOException {
    	String eventId = null;
    	if (args.length == 0) {
    		eventId = "3347";
    	} else {
    		eventId = args[0];
    	}
    	dataPathPrefix = "."+File.separator+"data"+File.separator+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+File.separator+eventId+File.separator;
    	 
    	String talksContent = getTalksJSON(eventId);
    	ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(talksContent);
        
        List<TalkRecord> talks = StreamSupport.stream(rootNode.path("talks").spliterator(), false)
        			 .map(TalkRecord::fromJsonNode)
        			 .collect(toList());
        
        
        for (TalkRecord t : talks) {		// Using external iteration is not FP-style programming, but in this point I preferred to start this way and possibly refactor later. 
        	LOG.info("Fetching for talk {} {} ", t.getId(), t.getTalk_title());
        	JsonNode commentNode = mapper.readTree(getCommentsJSON(eventId, t.getId()));
        	List<Comment> commentsForTalk = StreamSupport.stream(commentNode.path("comments").spliterator(), false)
        		.map(Comment::fromJsonNode)
        		.collect(new AvoidAnonDupCommentsCollector());
        	
        	Map<Boolean, Long> anonOrNot = commentsForTalk.stream()
        		.collect(partitioningBy(Comment::isAnon,
        								counting()));
        	        	
        	Map<Boolean, Double> anonPartAvg = commentsForTalk.stream()
	    		.collect(partitioningBy(Comment::isAnon,
	    								averagingInt(Comment::getRating)));
        	
        	Double totalAvg = commentsForTalk.stream()
    			.collect(averagingInt(Comment::getRating));
        	
        	TalkRecordStats talkRecordStats = new TalkRecordStats(t, anonOrNot, anonPartAvg, totalAvg);
        	LOG.info("Fetching for talk {} stats are {}", t.getId(), talkRecordStats);
        	appendToCsv(eventId, talkRecordStats);
        }
    }

	private static void appendToCsv(String eventId, TalkRecordStats talkRecordStats) {
		try {
			Path path = Paths.get(dataPathPrefix+"stats.csv");
			Files.write(path, talkRecordStats.toCvsLine().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			Files.write(path, "\n".getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			LOG.error("Unable to append stats {} to file.", talkRecordStats, e);
		}
	}

	private static String getTalksJSON(String eventId) {
		WebTarget target = client.target("http://api.joind.in/v2.1/events/"+eventId+"/talks?resultsperpage=0");
		LOG.info("About to get {}", target.getUri());
        Response response = target.request().get();
        String talksContent = response.readEntity(String.class);
        response.close();
        if (LOG.isDebugEnabled()) LOG.debug("response: {} ", talksContent);
        try {
        	Path path = Paths.get(dataPathPrefix+"talks.json");
        	Files.createDirectories(path.getParent());
			Files.write(path, talksContent.getBytes());
		} catch (Exception e) {
			LOG.warn("Unable to save target {} to file.", target.getUri(), e);
		}
		return talksContent;
	}
	
	private static String getCommentsJSON(String eventId, String talkId) {
		WebTarget target = client.target("http://api.joind.in/v2.1/talks/"+talkId+"/comments?resultsperpage=0");
		LOG.info("About to get {}", target.getUri());
        Response response = target.request().get();
        String talksContent = response.readEntity(String.class);
        response.close();
        if (LOG.isDebugEnabled()) LOG.debug("response: {} ", talksContent);
        try {
        	Path path = Paths.get(dataPathPrefix+"talk-"+talkId+"-comments.json");
        	Files.createDirectories(path.getParent());
			Files.write(path, talksContent.getBytes());
		} catch (Exception e) {
			LOG.warn("Unable to save target {} to file.", target.getUri(), e);
		}
		return talksContent;
	}
    
}
