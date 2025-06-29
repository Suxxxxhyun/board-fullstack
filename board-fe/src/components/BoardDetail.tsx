import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { 
  Card, 
  CardContent, 
  Typography, 
  Box, 
  CircularProgress,
  Button 
} from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import { Board } from '../types/board';
import { boardApi } from '../api/boardApi';

interface BoardDetailProps {
  onBack: () => void;
}

const BoardDetail: React.FC<BoardDetailProps> = ({ onBack }) => {
  const { id } = useParams<{ id: string }>();
  const [board, setBoard] = useState<Board | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBoard = async () => {
      if (!id) return;
      
      try {
        setLoading(true);
        const data = await boardApi.getBoardById(parseInt(id));
        setBoard(data);
      } catch (error) {
        console.error('게시글 조회 실패:', error);
        // 에러는 이미 API 인터셉터에서 alert 처리됨
      } finally {
        setLoading(false);
      }
    };

    fetchBoard();
  }, [id]);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
        <CircularProgress />
      </Box>
    );
  }

  if (!board) {
    return (
      <Box textAlign="center" py={4}>
        <Typography variant="h6" color="error">
          게시글을 찾을 수 없습니다.
        </Typography>
        <Button 
          variant="contained" 
          onClick={onBack}
          sx={{ mt: 2 }}
        >
          목록으로 돌아가기
        </Button>
      </Box>
    );
  }

  return (
    <Box>
      <Button 
        startIcon={<ArrowBack />}
        onClick={onBack}
        sx={{ mb: 2 }}
      >
        목록으로 돌아가기
      </Button>
      
      <Card>
        <CardContent>
          <Typography variant="h4" gutterBottom>
            {board.title}
          </Typography>
          
          <Box sx={{ mb: 2 }}>
            <Typography variant="body2" color="text.secondary">
              작성자: {board.author}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              작성일: {new Date(board.createdAt).toLocaleDateString()}
            </Typography>
          </Box>
          
          <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
            {board.content}
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
};

export default BoardDetail; 