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
import com.board.domain.service.strategy.LoadStrategy;
import com.board.domain.service.strategy.LoadStrategyType;
import com.board.domain.service.strategy.impl.InfiniteScrollStrategy;
import com.board.domain.service.strategy.impl.PagingStrategy;

@Service
@Transactional
public class BoardService {
	private final Map<LoadStrategyType, LoadStrategy> strategyMap;

	public BoardService(
		final List<LoadStrategy> strategies
	) {
		this.strategyMap = Map.of(
			LoadStrategyType.PAGING, resolve(strategies, PagingStrategy.class),
			LoadStrategyType.INFINITE, resolve(strategies, InfiniteScrollStrategy.class)
		);
	}

	@Transactional(readOnly = true)
	public Page<BoardDto.Response> getBoardsPaged(
		final LoadStrategyType type,
		final Optional<Long> cursorId,
		final Pageable pageable
	){
		LoadStrategy strategy = strategyMap.get(type);
		if (strategy == null) {
			throw new IllegalArgumentException("지원하지 않는 전략: " + type);
		}
		return strategy.load(pageable, cursorId);
	}
}
