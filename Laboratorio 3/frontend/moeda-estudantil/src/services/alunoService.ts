import api from './api';
import type { Aluno, Transacao, Vantagem, ResgateCupom } from '../types/aluno';
import { getStoredAuthData } from './auth';

const API_URL = '/api';

export const alunoService = {
  buscarMeusDados: async (): Promise<Aluno> => {
    const { user } = getStoredAuthData();
    if (!user) {
      throw new Error('Usuário não autenticado');
    }
    const response = await api.get(`${API_URL}/alunos/${user.id}`);
    return response.data;
  },

  listarVantagens: async (): Promise<Vantagem[]> => {
    const response = await api.get(`${API_URL}/vantagens`);
    return response.data;
  },

  listarTransacoes: async (): Promise<Transacao[]> => {
    const response = await api.get(`${API_URL}/transacoes/extrato`);
    return response.data;
  },

  buscarMeusCupons: async (): Promise<ResgateCupom[]> => {
    const response = await api.get(`${API_URL}/transacoes/cupons`);
    return response.data;
  }
}; 