package com.board.domain.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.board.domain.dto.BoardDto;
import com.board.domain.service.BoardService;
import com.board.domain.service.strategy.LoadStrategyType;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

	@MockitoBean
	private BoardService boardService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("페이징방식으로 게시글을 조회한다.")
	void getBoardsPaged() throws Exception{
		BoardDto.Response response = BoardDto.Response.builder()
			.id(1L)
			.author("test")
			.content("content")
			.title("title")
			.createdAt(LocalDateTime.now())
			.build();

		List<BoardDto.Response> content = Collections.singletonList(response);
		Page<BoardDto.Response> page = new PageImpl<>(content, PageRequest.of(0, 3), 1);

		// given
		given(boardService.getBoardsPaged(any(LoadStrategyType.class), any(), any(Pageable.class)))
			.willReturn(page);

		// when & then
		mockMvc.perform(get("/api/board")
				.param("type", "PAGING")
				.param("page", "0")
				.param("size", "3"))
			.andExpect(status().isOk());
	}
}