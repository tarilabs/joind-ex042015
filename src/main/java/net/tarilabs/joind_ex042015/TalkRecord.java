package net.tarilabs.joind_ex042015;

import com.fasterxml.jackson.databind.JsonNode;

public class TalkRecord {
	private String id;
	private String talk_title;
	private int h_average_rating;
	private int h_comment_count;
	private int h_starred_count;
	
	public TalkRecord(String id, String talk_title, int h_average_rating, int h_comment_count, int h_starred_count) {
		super();
		this.id = id;
		this.talk_title = talk_title;
		this.h_average_rating = h_average_rating;
		this.h_comment_count = h_comment_count;
		this.h_starred_count = h_starred_count;
	}

	public String getId() {
		return id;
	}

	public String getTalk_title() {
		return talk_title;
	}

	public int getH_avgerage_rating() {
		return h_average_rating;
	}

	public int getH_comment_count() {
		return h_comment_count;
	}

	public int getH_starred_count() {
		return h_starred_count;
	}
	
	public static TalkRecord fromJsonNode(JsonNode talk) {
		String id = talk.get("uri").textValue().split("talks/")[1];
		String talk_title = talk.get("talk_title").textValue();
		int h_average_rating = talk.get("average_rating").asInt(0);
		int h_comment_count = talk.get("comment_count").asInt(0);
		int h_starred_count = talk.get("starred_count").asInt(0);
		TalkRecord ret = new TalkRecord(id, talk_title, h_average_rating, h_comment_count, h_starred_count);
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TalkRecord [id=");
		builder.append(id);
		builder.append(", talk_title=");
		builder.append(talk_title);
		builder.append(", h_avgerage_rating=");
		builder.append(h_average_rating);
		builder.append(", h_comment_count=");
		builder.append(h_comment_count);
		builder.append(", h_starred_count=");
		builder.append(h_starred_count);
		builder.append("]");
		return builder.toString();
	}
	
	
}
