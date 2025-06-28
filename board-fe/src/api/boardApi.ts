import axios from 'axios';
import { Board, PageResponse, LoadStrategyType } from '../types/board';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const boardApi = {
  // 게시글 목록 조회 (페이징)
  getBoardsPaged: async (
    type: LoadStrategyType,
    page: number = 0,
    size: number = 3,
    cursorId?: number
  ): Promise<PageResponse<Board>> => {
    const params: any = {
      type,
      page,
      size,
    };
    
    if (cursorId) {
      params.cursorId = cursorId;
    }

    const response = await api.get('/board', { params });
    return response.data;
  },

  // 무한스크롤용 게시글 목록 조회
  getBoardsInfinite: async (
    cursorId?: number,
    size: number = 3
  ): Promise<PageResponse<Board>> => {
    const params: any = {
      type: LoadStrategyType.INFINITE,
      size,
    };
    
    if (cursorId) {
      params.cursorId = cursorId;
    }

    const response = await api.get('/board', { params });
    return response.data;
  },
}; 