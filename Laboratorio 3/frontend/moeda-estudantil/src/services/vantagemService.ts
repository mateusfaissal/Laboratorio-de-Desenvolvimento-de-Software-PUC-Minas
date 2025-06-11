import type { Vantagem, CadastroVantagemDTO } from '../types/vantagem';
import api from './api';
import { getStoredAuthData } from './auth';

const API_URL = '/api'; // O axios já usa o baseURL, então só precisa do path relativo

export const vantagemService = {
  cadastrarVantagem: async (vantagem: CadastroVantagemDTO): Promise<Vantagem> => {
    const { user } = getStoredAuthData();
    if (!user) {
      throw new Error('Usuário não autenticado');
    }
    
    const response = await api.post(`${API_URL}/vantagens/empresa/${user.id}`, {
      nome: vantagem.nome,
      descricao: vantagem.descricao,
      custoMoedas: vantagem.custoMoedas,
      fotoUrl: vantagem.fotoUrl
    });
    return response.data;
  },

  listarVantagens: async (): Promise<Vantagem[]> => {
    const response = await api.get(`${API_URL}/vantagens`);
    return response.data;
  },

  obterVantagemPorId: async (id: string): Promise<Vantagem> => {
    const response = await api.get(`${API_URL}/vantagens/${id}`);
    return response.data;
  },

  listarVantagensPorEmpresa: async (empresaId: string): Promise<Vantagem[]> => {
    const response = await api.get(`${API_URL}/vantagens/empresa/${empresaId}`);
    return response.data;
  },

  resgatarVantagem: async (vantagemId: string): Promise<any> => {
    const response = await api.post(`${API_URL}/vantagens/${vantagemId}/resgatar`);
    return response.data;
  }
}; 