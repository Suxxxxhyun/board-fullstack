import React, { useState, useEffect, useCallback, useRef } from 'react';
import {
  Container,
  Box,
  Typography,
  CircularProgress,
  Alert,
  Paper,
} from '@mui/material';
import { boardApi } from '../api/boardApi';
import { Board, LoadStrategyType, PageResponse } from '../types/board';
import BoardCard from './BoardCard';
import StrategyToggle from './StrategyToggle';
import Pagination from './Pagination';

const BoardList: React.FC = () => {
  const [boards, setBoards] = useState<Board[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [strategy, setStrategy] = useState<LoadStrategyType>(LoadStrategyType.PAGING);
  
  // 페이징 관련 상태
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  
  // 무한스크롤 관련 상태
  const [hasMore, setHasMore] = useState(true);
  const [lastCursorId, setLastCursorId] = useState<number | undefined>(undefined);
  
  const observer = useRef<IntersectionObserver>();
  const lastBoardElementRef = useCallback((node: HTMLDivElement) => {
    if (loading) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore && strategy === LoadStrategyType.INFINITE) {
        loadMoreBoards();
      }
    });
    if (node) observer.current.observe(node);
  }, [loading, hasMore, strategy]);

  const fetchBoards = async (page: number = 0, cursorId?: number) => {
    try {
      setLoading(true);
      setError(null);
      
      let response: PageResponse<Board>;
      
      if (strategy === LoadStrategyType.PAGING) {
        response = await boardApi.getBoardsPaged(strategy, page, 3, cursorId);
        setBoards(response.content);
        setCurrentPage(response.number);
        setTotalPages(response.totalPages);
        setTotalElements(response.totalElements);
      } else {
        response = await boardApi.getBoardsInfinite(cursorId, 3);
        if (page === 0) {
          setBoards(response.content);
        } else {
          setBoards(prev => [...prev, ...response.content]);
        }
        setLastCursorId(response.content[response.content.length - 1]?.id);
        setHasMore(!response.last);
      }
    } catch (err) {
      setError('게시글을 불러오는 중 오류가 발생했습니다.');
      console.error('Error fetching boards:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadMoreBoards = () => {
    if (strategy === LoadStrategyType.INFINITE && hasMore) {
      fetchBoards(currentPage + 1, lastCursorId);
    }
  };

  const handleStrategyChange = (newStrategy: LoadStrategyType) => {
    setStrategy(newStrategy);
    setCurrentPage(0);
    setBoards([]);
    setHasMore(true);
    setLastCursorId(undefined);
    setError(null);
    
    if (newStrategy === LoadStrategyType.PAGING) {
      fetchBoards(0);
    } else {
      fetchBoards(0);
    }
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    fetchBoards(page);
  };

  useEffect(() => {
    fetchBoards(0);
  }, []);

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h4" component="h1" gutterBottom>
            게시판
          </Typography>
          <StrategyToggle 
            strategy={strategy} 
            onStrategyChange={handleStrategyChange} 
          />
        </Box>
        
        {strategy === LoadStrategyType.PAGING && (
          <Typography variant="body2" color="text.secondary">
            총 {totalElements}개의 게시글
          </Typography>
        )}
      </Paper>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Box>
        {boards.map((board, index) => {
          if (strategy === LoadStrategyType.INFINITE && boards.length === index + 1) {
            return (
              <div key={board.id} ref={lastBoardElementRef}>
                <BoardCard board={board} />
              </div>
            );
          } else {
            return <BoardCard key={board.id} board={board} />;
          }
        })}
      </Box>

      {loading && (
        <Box display="flex" justifyContent="center" sx={{ mt: 3 }}>
          <CircularProgress />
        </Box>
      )}

      {strategy === LoadStrategyType.PAGING && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageChange}
        />
      )}

      {strategy === LoadStrategyType.INFINITE && !hasMore && boards.length > 0 && (
        <Box textAlign="center" sx={{ mt: 3 }}>
          <Typography variant="body2" color="text.secondary">
            모든 게시글을 불러왔습니다.
          </Typography>
        </Box>
      )}
    </Container>
  );
};

export default BoardList; 