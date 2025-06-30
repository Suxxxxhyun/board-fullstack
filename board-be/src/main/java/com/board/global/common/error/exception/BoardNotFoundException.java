package com.board.global.common.error.exception;

import com.board.global.common.error.ErrorCode;

public class BoardNotFoundException extends BusinessException{

	public BoardNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
