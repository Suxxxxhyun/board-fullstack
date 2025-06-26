package com.board.domain.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.board.domain.dto.BoardDto;
import com.board.domain.service.BoardService;
import com.board.domain.service.strategy.LoadStrategy;
import com.board.domain.service.strategy.LoadStrategyType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;

	@GetMapping
	public Page<BoardDto.Response> getBoardsPaged(
		@RequestParam LoadStrategyType type,
		@RequestParam Optional<Long> cursorId,
		@PageableDefault(size = 3) final Pageable pageable
	){
		return boardService.getBoardsPaged(type, cursorId, pageable);
	}
}
