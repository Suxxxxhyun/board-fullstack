package com.board.domain.service.strategy;

import java.util.List;

public enum LoadStrategyType {
	PAGING,
	INFINITE;

	private LoadStrategy getStrategy(List<LoadStrategy> strategies, Class<? extends LoadStrategy> type) {
		return strategies.stream()
			.filter(type::isInstance)
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("전략을 찾을 수 없습니다: " + type.getSimpleName()));
	}
}
