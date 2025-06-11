import type { SignInCredentials, User } from '../types';
import api from './api';

interface SignInResponse {
  user: User;
  token: string;
}

export async function signIn(credentials: SignInCredentials): Promise<SignInResponse> {
  // Envia o campo 'senha' para o backend
  const response = await api.post('/api/auth/login', {
    email: credentials.email,
    senha: credentials.senha,
  });
  const { token, ...user } = response.data;
  console.log('Token recebido:', token); // Debug
  return { user, token };
}

export function storeAuthData(data: SignInResponse): void {
  console.log('Armazenando token:', data.token); // Debug
  localStorage.setItem('@StudentCoin:token', data.token);
  localStorage.setItem('@StudentCoin:user', JSON.stringify(data.user));
}

export function getStoredAuthData(): { user: User | null; token: string | null } {
  const token = localStorage.getItem('@StudentCoin:token');
  const storedUser = localStorage.getItem('@StudentCoin:user');
  
  console.log('Token armazenado:', token); // Debug
  
  const user = storedUser ? JSON.parse(storedUser) as User : null;
  
  return { user, token };
}

export function clearAuthData(): void {
  localStorage.removeItem('@StudentCoin:token');
  localStorage.removeItem('@StudentCoin:user');
}