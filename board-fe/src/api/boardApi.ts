import axios from 'axios';
import { Board, PageResponse, LoadStrategyType, ErrorResponse } from '../types/board';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 에러 처리 인터셉터
api.interceptors.response.use(
  (response: any) => response,
  (error: any) => {
    if (error.response) {
      const errorData = error.response.data;
      if (errorData) {
        console.error(`에러: ${errorData.message}`);
      } else {
        console.error(`에러: ${error.response.status} ${error.response.statusText}`);
      }
    } else if (error.request) {
      alert('서버에 연결할 수 없습니다.');
    } else {
      alert('요청 중 오류가 발생했습니다.');
    }
    return Promise.reject(error);
  }
);

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
    return response.data as PageResponse<Board>;
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
    return response.data as PageResponse<Board>;
  },

  // 단일 게시글 조회
  getBoardById: async (id: number): Promise<Board> => {
    const response = await api.get(`/board/${id}`);
    return response.data as Board;
  },
}; 