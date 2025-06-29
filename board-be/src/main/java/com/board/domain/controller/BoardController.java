package com.board.domain.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.board.domain.dto.BoardDto;
import com.board.domain.service.BoardService;
import com.board.domain.swagger.BoardSwagger;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController implements BoardSwagger {
	private final BoardService boardService;

	@GetMapping
	public Page<BoardDto.Response> getBoardsPaged(
		@Valid @ModelAttribute final BoardDto.Request request,
		@PageableDefault(size = 3) final Pageable pageable
	){
		return boardService.getBoardsPaged(request.type(), request.cursorId(), pageable);
	}

	@GetMapping("/{id}")
	public BoardDto.Response getBoardById(@PathVariable final Long id) {
		return boardService.getBoardById(id);
	}
}
