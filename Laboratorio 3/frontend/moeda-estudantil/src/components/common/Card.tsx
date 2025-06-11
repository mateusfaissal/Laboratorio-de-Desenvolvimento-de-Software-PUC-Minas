import React from 'react';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  title?: string;
}

export const Card: React.FC<CardProps> = ({ children, className = '', title }) => {
  return (
    <div className={`bg-dark-gray-800 border border-dark-gray-700 rounded-lg p-6 ${className}`}>
      {title && <h2 className="text-xl font-semibold text-dark-gray-100 mb-4">{title}</h2>}
      {children}
    </div>
  );
};