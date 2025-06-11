export interface Vantagem {
  id?: string;
  nome: string;
  descricao: string;
  custoMoedas: number;
  fotoUrl?: string;
  empresaId: string;
}

export interface CadastroVantagemDTO {
  nome: string;
  descricao: string;
  custoMoedas: number;
  fotoUrl?: string;
} 