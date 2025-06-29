package com.board.domain.service;

import static com.board.domain.service.strategy.StrategyResolver.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.domain.dto.BoardDto;
import com.board.domain.entity.Board;
import com.board.domain.repository.BoardRepository;
import com.board.domain.service.strategy.LoadStrategy;
import com.board.domain.service.strategy.LoadStrategyType;
import com.board.domain.service.strategy.impl.InfiniteScrollStrategy;
import com.board.domain.service.strategy.impl.PagingStrategy;
import com.board.global.common.error.ErrorCode;
import com.board.global.common.error.exception.BusinessException;

@Service
@Transactional
public class BoardService {
	private final Map<LoadStrategyType, LoadStrategy> strategyMap;
	private final BoardRepository boardRepository;

	public BoardService(
		final List<LoadStrategy> strategies,
		final BoardRepository boardRepository
	) {
		this.strategyMap = Map.of(
			LoadStrategyType.PAGING, resolve(strategies, PagingStrategy.class),
			LoadStrategyType.INFINITE, resolve(strategies, InfiniteScrollStrategy.class)
		);
		this.boardRepository = boardRepository;
	}

	@Transactional(readOnly = true)
	public Page<BoardDto.Response> getBoardsPaged(
		final LoadStrategyType type,
		final Optional<Long> cursorId,
		final Pageable pageable
	){
		LoadStrategy strategy = strategyMap.get(type);
		return strategy.load(pageable, cursorId);
	}

	@Transactional(readOnly = true)
	public BoardDto.Response getBoardById(final Long id) {
		Board board = boardRepository.findById(id)
			.orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
		return BoardDto.Response.from(board);
	}
}
