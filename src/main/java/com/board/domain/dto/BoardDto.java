package com.board.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.board.domain.entity.Board;

public class BoardDto {
	public record Response(
		Long id,
		String title,
		String content,
		String author,
		LocalDateTime createdAt
	){
		public static Response from(Board board){
			return new Response(
				board.getId(),
				board.getTitle(),
				board.getContent(),
				board.getAuthor().getName(),
				board.getCreatedAt()
			);
		}
	}
}
