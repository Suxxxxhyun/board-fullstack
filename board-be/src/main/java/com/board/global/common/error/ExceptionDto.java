package com.board.global.common.error;

import jakarta.validation.constraints.NotNull;

public record ExceptionDto(
	@NotNull ErrorCode code,

	@NotNull String message
) {
}
