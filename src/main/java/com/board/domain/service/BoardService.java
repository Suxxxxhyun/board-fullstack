package com.board.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.domain.dto.BoardDto;
import com.board.domain.entity.Board;
import com.board.domain.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;

	@Transactional(readOnly = true)
	public Page<BoardDto.Response> getBoardsPaged(
		final Pageable pageable
	){
		Page<Board> boardsPaged = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
		return boardsPaged.map(BoardDto.Response::from);
	}
}
