import React from 'react';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  title?: string;
}

export const Card: React.FC<CardProps> = ({ children, className = '', title }) => {
  return (
    <div className={`bg-dark-gray-800 rounded-xl border border-dark-gray-700 shadow-lg overflow-hidden ${className}`}>
      {title && (
        <div className="px-6 py-4 border-b border-dark-gray-700">
          <h3 className="text-lg font-medium text-dark-gray-100">{title}</h3>
        </div>
      )}
      <div className="p-6">{children}</div>
    </div>
  );
};