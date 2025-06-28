package com.board.domain.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import com.board.domain.entity.Board;
import com.board.domain.service.strategy.LoadStrategyType;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class BoardDto {
	public record Request(
		@NotNull
		LoadStrategyType type,
		Optional<Long> cursorId
	){

	}

	@Builder
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
