import type { ReactNode } from 'react';

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'student' | 'professor' | 'partner';
}

export interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
}

export interface AuthContextData extends AuthState {
  signIn: (credentials: SignInCredentials) => Promise<void>;
  signOut: () => void;
}

export interface AuthProviderProps {
  children: ReactNode;
}

export interface SignInCredentials {
  email: string;
  password: string;
}

export interface Student {
  id: string;
  name: string;
  email: string;
  cpf: string;
  rg: string;
  address: string;
  institution: string;
  course: string;
}

export interface Professor {
  id: string;
  name: string;
  email: string;
  cpf: string;
  department: string;
  institution: string;
  coinBalance: number;
}

export interface Transaction {
  id: string;
  sender_id?: string;
  sender_name?: string;
  receiver_id?: string;
  receiver_name?: string;
  amount: number;
  description: string;
  created_at: string;
  type: 'sent' | 'received';
}

export interface SendCoinFormData {
  student_id: string;
  amount: number;
  description: string;
}