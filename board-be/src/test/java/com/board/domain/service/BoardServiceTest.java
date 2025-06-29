package com.board.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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
import com.board.domain.entity.Author;
import com.board.domain.entity.Board;
import com.board.domain.repository.BoardRepository;
import com.board.domain.service.strategy.LoadStrategyType;
import com.board.domain.service.strategy.impl.InfiniteScrollStrategy;
import com.board.domain.service.strategy.impl.PagingStrategy;
import com.board.global.common.error.ErrorCode;
import com.board.global.common.error.exception.BusinessException;

class BoardServiceTest {

	private BoardService boardService;
	private InfiniteScrollStrategy infiniteScrollStrategy;
	private PagingStrategy pagingStrategy;
	private BoardRepository boardRepository;

	@BeforeEach
	void setUp() {
		pagingStrategy = mock(PagingStrategy.class);
		infiniteScrollStrategy = mock(InfiniteScrollStrategy.class);
		boardRepository = mock(BoardRepository.class);

		// 전략들을 List로 전달해 서비스 생성
		boardService = new BoardService(List.of(pagingStrategy, infiniteScrollStrategy), boardRepository);
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

	@Test
	@DisplayName("존재하는 게시글 ID로 조회 시 게시글을 반환한다.")
	void getBoardById_withExistingId() {
		// given
		Long boardId = 1L;
		Board board = mock(Board.class);
		Author author = mock(Author.class);
		
		when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
		when(board.getId()).thenReturn(boardId);
		when(board.getTitle()).thenReturn("테스트 제목");
		when(board.getContent()).thenReturn("테스트 내용");
		when(board.getAuthor()).thenReturn(author);
		when(board.getCreatedAt()).thenReturn(LocalDateTime.now());
		when(author.getName()).thenReturn("테스트 작성자");

		// when
		BoardDto.Response result = boardService.getBoardById(boardId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(boardId);
		assertThat(result.title()).isEqualTo("테스트 제목");
		assertThat(result.content()).isEqualTo("테스트 내용");
		assertThat(result.author()).isEqualTo("테스트 작성자");
		verify(boardRepository).findById(boardId);
	}

	@Test
	@DisplayName("존재하지 않는 게시글 ID로 조회 시 BusinessException을 발생시킨다.")
	void getBoardById_withNonExistingId() {
		// given
		Long boardId = 999L;
		when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> boardService.getBoardById(boardId))
			.isInstanceOf(BusinessException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.BOARD_NOT_FOUND);
	}
}