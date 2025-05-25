import type { Vantagem, CadastroVantagemDTO } from '../types/vantagem';
import { vantagensMock } from '../mocks/vantagensMock';

// Simula um delay de rede
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// Copia inicial dos dados mock
let vantagens = [...vantagensMock];

export const vantagemServiceMock = {
  cadastrarVantagem: async (vantagem: CadastroVantagemDTO): Promise<Vantagem> => {
    await delay(500); // Simula delay da rede
    
    const novaVantagem: Vantagem = {
      ...vantagem,
      id: (vantagens.length + 1).toString(),
      empresaId: 'empresa_mock_' + Date.now()
    };
    
    vantagens.push(novaVantagem);
    return novaVantagem;
  },

  listarVantagens: async (): Promise<Vantagem[]> => {
    await delay(800); // Simula delay da rede
    return vantagens;
  },

  obterVantagemPorId: async (id: string): Promise<Vantagem> => {
    await delay(300); // Simula delay da rede
    const vantagem = vantagens.find(v => v.id === id);
    if (!vantagem) {
      throw new Error('Vantagem não encontrada');
    }
    return vantagem;
  },

  listarVantagensPorEmpresa: async (empresaId: string): Promise<Vantagem[]> => {
    await delay(600); // Simula delay da rede
    return vantagens.filter(v => v.empresaId === empresaId);
  }
}; 