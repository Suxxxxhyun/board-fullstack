import React from 'react';
import {
  ToggleButton,
  ToggleButtonGroup,
  Typography,
  Box,
} from '@mui/material';
import { LoadStrategyType } from '../types/board';

interface StrategyToggleProps {
  strategy: LoadStrategyType;
  onStrategyChange: (strategy: LoadStrategyType) => void;
}

const StrategyToggle: React.FC<StrategyToggleProps> = ({ 
  strategy, 
  onStrategyChange 
}) => {
  const handleChange = (
    event: React.MouseEvent<HTMLElement>,
    newStrategy: LoadStrategyType | null,
  ) => {
    if (newStrategy !== null) {
      onStrategyChange(newStrategy);
    }
  };

  return (
    <Box display="flex" alignItems="center" gap={2}>
      <Typography variant="body2" color="text.secondary">
        로딩 방식:
      </Typography>
      <ToggleButtonGroup
        value={strategy}
        exclusive
        onChange={handleChange}
        aria-label="loading strategy"
        size="small"
      >
        <ToggleButton 
          value={LoadStrategyType.PAGING} 
          aria-label="paging"
          sx={{ 
            minWidth: 80,
            '&.Mui-selected': {
              backgroundColor: 'primary.main',
              color: 'white',
              '&:hover': {
                backgroundColor: 'primary.dark',
              },
            },
          }}
        >
          페이징
        </ToggleButton>
        <ToggleButton 
          value={LoadStrategyType.INFINITE} 
          aria-label="infinite scroll"
          sx={{ 
            minWidth: 80,
            '&.Mui-selected': {
              backgroundColor: 'primary.main',
              color: 'white',
              '&:hover': {
                backgroundColor: 'primary.dark',
              },
            },
          }}
        >
          무한스크롤
        </ToggleButton>
      </ToggleButtonGroup>
    </Box>
  );
};

export default StrategyToggle; 