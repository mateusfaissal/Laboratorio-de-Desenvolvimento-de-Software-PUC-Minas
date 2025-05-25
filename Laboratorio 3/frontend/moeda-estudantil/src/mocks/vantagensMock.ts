import type { Vantagem } from '../types/vantagem';

export const vantagensMock: Vantagem[] = [
  {
    id: '1',
    titulo: 'Desconto de 50% na Cantina',
    descricao: 'Vale um desconto de 50% em qualquer refeição na cantina universitária. Válido por 30 dias.',
    custo: 100,
    foto: 'https://images.unsplash.com/photo-1498837167922-ddd27525d352?q=80&w=500',
    empresaId: 'cantina123'
  },
  {
    id: '2',
    titulo: 'Vale Livros na Livraria Central',
    descricao: 'Voucher de R$ 50,00 para compra de livros na Livraria Central do campus. Não válido para livros didáticos.',
    custo: 150,
    foto: 'https://images.unsplash.com/photo-1507842217343-583bb7270b66?q=80&w=500',
    empresaId: 'livraria456'
  },
  {
    id: '3',
    titulo: 'Ingresso para Festa Universitária',
    descricao: 'Um ingresso para a próxima festa universitária oficial. Inclui open bar de água e refrigerante.',
    custo: 200,
    foto: 'https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?q=80&w=500',
    empresaId: 'eventos789'
  },
  {
    id: '4',
    titulo: 'Desconto em Material Escolar',
    descricao: '30% de desconto em toda a linha de materiais escolares da Papelaria do Campus.',
    custo: 80,
    foto: 'https://images.unsplash.com/photo-1452860606245-08befc0ff44b?q=80&w=500',
    empresaId: 'papelaria101'
  },
  {
    id: '5',
    titulo: 'Curso de Inglês - 1 Mês Grátis',
    descricao: 'Um mês gratuito de curso de inglês na escola parceira. Válido para novos alunos.',
    custo: 300,
    foto: 'https://images.unsplash.com/photo-1546410531-bb4caa6b424d?q=80&w=500',
    empresaId: 'ingles202'
  }
]; 