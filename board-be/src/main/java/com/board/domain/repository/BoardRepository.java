package com.board.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.board.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	Page<Board> findAllByOrderByCreatedAtDesc(final Pageable pageable);
	Page<Board> findByIdLessThanOrderByIdDesc(final Long lastId, final Pageable pageable);
}
