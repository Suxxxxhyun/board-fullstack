package com.board.domain.swagger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import com.board.domain.dto.BoardDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Board API", description = "게시글 정보 관련 API")
public interface BoardSwagger {
	@Operation(summary = "게시글 목록 조회", description = "게시글 목록을 페이징으로 조회합니다.")
	@ApiResponse(
		responseCode = "200",
		description = "조회 성공",
		content = @Content(
			schema = @Schema(implementation = BoardDto.Response.class))
	)
	Page<BoardDto.Response> getBoardsPaged(
		final BoardDto.Request request,
		final Pageable pageable);

	@Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(
				schema = @Schema(implementation = BoardDto.Response.class))
		),
		@ApiResponse(
			responseCode = "404",
			description = "조회 실패"
		)
	})
	BoardDto.Response getBoardById(final Long id);
}
