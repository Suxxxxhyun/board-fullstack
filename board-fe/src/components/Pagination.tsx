import React from 'react';
import {
  Pagination as MuiPagination,
  Box,
  Typography,
} from '@mui/material';

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
}) => {
  const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
    onPageChange(value - 1); // MUI Pagination은 1부터 시작하므로 0부터 시작하는 페이지로 변환
  };

  if (totalPages <= 1) {
    return null;
  }

  return (
    <Box 
      display="flex" 
      flexDirection="column" 
      alignItems="center" 
      gap={1}
      sx={{ mt: 3, mb: 2 }}
    >
      <Typography variant="body2" color="text.secondary">
        {currentPage + 1} / {totalPages} 페이지
      </Typography>
      <MuiPagination
        count={totalPages}
        page={currentPage + 1}
        onChange={handleChange}
        color="primary"
        size="large"
        showFirstButton
        showLastButton
      />
    </Box>
  );
};

export default Pagination; 