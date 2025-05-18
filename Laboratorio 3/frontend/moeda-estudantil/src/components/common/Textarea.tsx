import React from 'react';
import type { TextareaHTMLAttributes } from 'react';
interface TextareaProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  label: string;
  name: string;
  error?: string;
}

export const Textarea: React.FC<TextareaProps> = ({
  label,
  name,
  error,
  className = '',
  ...rest
}) => {
  return (
    <div className="mb-4">
      <label
        htmlFor={name}
        className="block text-sm font-medium text-gray-700 mb-1"
      >
        {label}
      </label>
      
      <textarea
        id={name}
        name={name}
        className={`
          w-full px-3 py-2 border rounded-md shadow-sm 
          ${error ? 'border-red-500 focus:ring-red-500 focus:border-red-500' : 'border-gray-300 focus:ring-blue-500 focus:border-blue-500'} 
          ${className}
        `}
        {...rest}
      />
      
      {error && (
        <p className="mt-1 text-sm text-red-600">{error}</p>
      )}
    </div>
  );
};