package com.board.domain.entity;

import org.apache.tomcat.util.http.parser.Authorization;
import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Board extends BaseTimeEntity{
	@Id
	@Column(name = "board_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("제목")
	@Column(nullable = false)
	private String title;

	@Comment("내용")
	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;
}
