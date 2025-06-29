import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, useParams, useNavigate } from 'react-router-dom';
import BoardList from './components/BoardList';
import BoardDetail from './components/BoardDetail';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<BoardList />} />
        <Route path="/board/:id" element={<BoardDetailWrapper />} />
      </Routes>
    </Router>
  );
};

// BoardDetail을 위한 래퍼 컴포넌트 (useNavigate 사용을 위해)
const BoardDetailWrapper: React.FC = () => {
  const navigate = useNavigate();
  
  const handleBack = () => {
    navigate('/');
  };
  
  return <BoardDetail onBack={handleBack} />;
};

export default App; 