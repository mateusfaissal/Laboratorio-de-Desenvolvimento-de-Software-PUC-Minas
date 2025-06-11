import React from 'react';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
}

export const Input: React.FC<InputProps> = ({
  label,
  id,
  className = '',
  ...props
}) => {
  return (
    <div>
      <label htmlFor={id} className="block text-sm font-medium text-dark-gray-300 mb-1">
        {label}
      </label>
      <input
        id={id}
        className={`input-field ${className}`}
        {...props}
      />
    </div>
  );
};