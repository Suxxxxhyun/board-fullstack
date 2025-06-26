package com.board.domain.service.strategy.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.board.domain.dto.BoardDto;
import com.board.domain.entity.Board;
import com.board.domain.repository.BoardRepository;
import com.board.domain.service.strategy.LoadStrategy;

import lombok.RequiredArgsConstructor;

@Component
@Qualifier("pagingStrategy")
@RequiredArgsConstructor
public class PagingStrategy implements LoadStrategy {

	private final BoardRepository boardRepository;

	@Override
	public Page<BoardDto.Response> load(Pageable pageable, Optional<Long> cursorId) {
		Page<Board> boardsPaged = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
		return boardsPaged.map(BoardDto.Response::from);
	}
}
