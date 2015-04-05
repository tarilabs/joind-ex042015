package net.tarilabs.joind_ex042015;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;

public class Comment {
	private int rating;
	private String comment;
	private String user_display_name;
	private LocalDateTime created_date;
	
	public Comment(int rating, String comment, String user_display_name, LocalDateTime created_date) {
		super();
		this.rating = rating;
		this.comment = comment;
		this.user_display_name = user_display_name;
		this.created_date = created_date;
	}

	public int getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

	public String getUser_display_name() {
		return user_display_name;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}
	
	public static Comment fromJsonNode(JsonNode node) {
		int rating = node.get("rating").asInt(0);
		String comment = node.get("comment").asText();
		String user_display_name = node.get("user_display_name").asText();
		LocalDateTime created_date = LocalDateTime.parse( node.get("created_date").asText(), DateTimeFormatter.ISO_DATE_TIME);
		Comment ret = new Comment(rating, comment, user_display_name, created_date);
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Comment [rating=");
		builder.append(rating);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", user_display_name=");
		builder.append(user_display_name);
		builder.append(", created_date=");
		builder.append(created_date);
		builder.append("]");
		return builder.toString();
	}

	public boolean isAnon() {
		return user_display_name == null || "".equals(user_display_name) || "null".equals(user_display_name);
	}

	public boolean isEmptyText() {
		return comment == null || "".equals(comment);
	}
	
	
}
