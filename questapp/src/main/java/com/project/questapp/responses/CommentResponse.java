package com.project.questapp.responses;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.questapp.entities.Comment;

import lombok.Data;

@Data
public class CommentResponse {
	
	Long id;
	Long userId;
	String text;
	String username;
	
	@Autowired
	public CommentResponse(Comment entity) {
		this.id = entity.getId();
		this.userId = entity.getUser().getId();
		this.text = entity.getText();
		this.username = entity.getUser().getUserName();
	} 

}
