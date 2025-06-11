export interface Aluno {
  id: string;
  nome: string;
  email: string;
  saldoMoedas: number;
  curso?: {
    id: string;
    nome: string;
  };
}

export interface Transacao {
  id: string;
  dataHora: string;
  valor: number;
  tipo: 'TRANSFERENCIA' | 'RESGATE';
  descricao: string;
  aluno?: {
    id: string;
    nome: string;
    email: string;
    cpf: string;
    rg: string;
    endereco: string;
    saldoMoedas: number;
    tipo: string;
    curso?: {
      id: string;
      nome: string;
      instituicao: string;
    };
  };
  professor?: {
    id: string;
    nome: string;
    email: string;
    cpf: string;
    saldoMoedas: number;
    tipo: string;
    departamento?: {
      id: string;
      nome: string;
      instituicao: any;
    };
  };
  motivoReconhecimento?: string;
}

export interface Vantagem {
  id: string;
  nome: string;
  descricao: string;
  custoMoedas: number;
  fotoUrl: string | null;
  empresa: {
    id: string;
    nome: string;
    email: string;
    cpf: string;
    cnpj: string;
    descricao: string;
    tipo: string;
    vantagens: string[];
  };
}

export interface ResgateCupom {
  id: string;
  dataUtilizacao: string;
  valor: number; // Valor em moedas que foi custo da vantagem
  descricao: string;
  utilizado: boolean;
  codigoGerado: string;
  aluno: { // Pode ser um subconjunto dos dados do Aluno, dependendo do retorno do backend
    id: string;
    nome: string;
    email: string;
  };
  vantagem: { // Pode ser um subconjunto dos dados da Vantagem
    id: string;
    nome: string;
    descricao: string;
    fotoUrl?: string | null;
  };
} 