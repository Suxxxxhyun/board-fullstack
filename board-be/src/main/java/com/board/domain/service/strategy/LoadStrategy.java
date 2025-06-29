package com.board.domain.service.strategy;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.board.domain.dto.BoardDto;

public interface LoadStrategy {
	Page<BoardDto.Response> load(Pageable pageable, Optional<Long> cursorId);
}
