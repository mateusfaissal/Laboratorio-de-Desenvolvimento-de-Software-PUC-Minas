import type { Vantagem, CadastroVantagemDTO } from '../types/vantagem';
import { vantagemServiceMock } from './vantagemServiceMock';

// Usando o serviço mock para desenvolvimento
export const vantagemService = vantagemServiceMock;

// Código da API real (comentado por enquanto)
/*
const API_URL = 'http://localhost:3000/api';

export const vantagemService = {
  cadastrarVantagem: async (vantagem: CadastroVantagemDTO): Promise<Vantagem> => {
    const response = await axios.post(`${API_URL}/vantagens`, vantagem);
    return response.data;
  },

  listarVantagens: async (): Promise<Vantagem[]> => {
    const response = await axios.get(`${API_URL}/vantagens`);
    return response.data;
  },

  obterVantagemPorId: async (id: string): Promise<Vantagem> => {
    const response = await axios.get(`${API_URL}/vantagens/${id}`);
    return response.data;
  },

  listarVantagensPorEmpresa: async (empresaId: string): Promise<Vantagem[]> => {
    const response = await axios.get(`${API_URL}/vantagens/empresa/${empresaId}`);
    return response.data;
  }
};
*/ 