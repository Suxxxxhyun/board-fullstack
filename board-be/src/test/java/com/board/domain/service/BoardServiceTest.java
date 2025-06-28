package com.board.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.board.domain.dto.BoardDto;
import com.board.domain.service.strategy.LoadStrategyType;
import com.board.domain.service.strategy.impl.InfiniteScrollStrategy;
import com.board.domain.service.strategy.impl.PagingStrategy;

class BoardServiceTest {

	private BoardService boardService;
	private InfiniteScrollStrategy infiniteScrollStrategy;
	private PagingStrategy pagingStrategy;

	@BeforeEach
	void setUp() {
		pagingStrategy = mock(PagingStrategy.class);
		infiniteScrollStrategy = mock(InfiniteScrollStrategy.class);

		// 전략들을 List로 전달해 서비스 생성
		boardService = new BoardService(List.of(pagingStrategy, infiniteScrollStrategy));
	}

	@Test
	@DisplayName("PAGING 전략으로 게시글 조회 시 PagingStrategy 사용한다.")
	void getBoardsPaged_withPagingStrategy() {
		// given
		Page<BoardDto.Response> expected = new PageImpl<>(List.of());
		when(pagingStrategy.load(any(Pageable.class), any())).thenReturn(expected);

		// when
		Page<BoardDto.Response> result = boardService.getBoardsPaged(
			LoadStrategyType.PAGING, Optional.empty(), PageRequest.of(0, 3));

		// then
		assertThat(result).isEqualTo(expected);
	}

	@Test
	@DisplayName("INFINITE 전략으로 게시글 조회 시 InfiniteScrollStrategy 사용한다.")
	void getBoardsPaged_withInfiniteScrollStrategy() {
		// given
		Page<BoardDto.Response> expected = new PageImpl<>(List.of());
		when(infiniteScrollStrategy.load(any(Pageable.class), any())).thenReturn(expected);

		// when
		Page<BoardDto.Response> result = boardService.getBoardsPaged(
			LoadStrategyType.INFINITE, Optional.empty(), PageRequest.of(0, 3));

		// then
		assertThat(result).isEqualTo(expected);
	}
}