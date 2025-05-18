import type { SignInCredentials, User } from '../types';

interface SignInResponse {
  user: User;
  token: string;
}

// Usuários mockados para teste
const MOCK_USERS = {
  professor: {
    id: '1',
    name: 'Professor Teste',
    email: 'professor@teste.com',
    role: 'professor' as const,
  },
  student: {
    id: '2',
    name: 'Estudante Teste',
    email: 'estudante@teste.com',
    role: 'student' as const,
  }
};

// Senha padrão para teste
const TEST_PASSWORD = '123456';

export async function signIn(credentials: SignInCredentials): Promise<SignInResponse> {
  // Simula um delay de rede
  await new Promise(resolve => setTimeout(resolve, 1000));

  // Verifica se é um usuário de teste
  const mockUser = Object.values(MOCK_USERS).find(user => user.email === credentials.email);

  if (mockUser && credentials.password === TEST_PASSWORD) {
    return {
      user: mockUser,
      token: 'mock-jwt-token'
    };
  }

  throw new Error('Credenciais inválidas');
}

export function storeAuthData(data: SignInResponse): void {
  localStorage.setItem('@StudentCoin:token', data.token);
  localStorage.setItem('@StudentCoin:user', JSON.stringify(data.user));
}

export function getStoredAuthData(): { user: User | null; token: string | null } {
  const token = localStorage.getItem('@StudentCoin:token');
  const storedUser = localStorage.getItem('@StudentCoin:user');
  
  const user = storedUser ? JSON.parse(storedUser) as User : null;
  
  return { user, token };
}

export function clearAuthData(): void {
  localStorage.removeItem('@StudentCoin:token');
  localStorage.removeItem('@StudentCoin:user');
}