export interface Vantagem {
  id?: string;
  titulo: string;
  descricao: string;
  custo: number;
  foto: string;
  empresaId: string;
}

export interface CadastroVantagemDTO {
  titulo: string;
  descricao: string;
  custo: number;
  foto: string;
} 