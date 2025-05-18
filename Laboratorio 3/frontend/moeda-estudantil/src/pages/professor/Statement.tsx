import React, { useState } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';

// Dados mockados
const MOCK_TRANSACTIONS = [
  {
    id: '1',
    aluno: 'Maria Silva',
    valor: 50,
    data: '2024-03-15',
    descricao: 'Participação ativa em aula',
    curso: 'Engenharia de Software'
  },
  {
    id: '2',
    aluno: 'João Santos',
    valor: 30,
    data: '2024-03-14',
    descricao: 'Ajuda aos colegas',
    curso: 'Ciência da Computação'
  },
  {
    id: '3',
    aluno: 'Ana Oliveira',
    valor: 40,
    data: '2024-03-13',
    descricao: 'Excelente apresentação',
    curso: 'Sistemas de Informação'
  },
  {
    id: '4',
    aluno: 'Pedro Costa',
    valor: 25,
    data: '2024-03-12',
    descricao: 'Contribuição em projeto',
    curso: 'Engenharia de Software'
  },
  {
    id: '5',
    aluno: 'Beatriz Lima',
    valor: 35,
    data: '2024-03-11',
    descricao: 'Resolução de exercícios',
    curso: 'Ciência da Computação'
  }
];

export const Statement: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedMonth, setSelectedMonth] = useState('');

  const filteredTransactions = MOCK_TRANSACTIONS.filter(transaction => {
    const matchesSearch = transaction.aluno.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         transaction.descricao.toLowerCase().includes(searchTerm.toLowerCase());
    
    if (!selectedMonth) return matchesSearch;
    
    const transactionMonth = new Date(transaction.data).getMonth();
    return matchesSearch && transactionMonth === parseInt(selectedMonth);
  });

  const totalMoedas = filteredTransactions.reduce((sum, transaction) => sum + transaction.valor, 0);

  return (
    <MainLayout>
      <div className="space-y-6">
        {/* Cabeçalho */}
        <div>
          <h1 className="text-2xl font-bold text-dark-gray-100">Extrato de Moedas</h1>
          <p className="mt-1 text-dark-gray-400">Histórico de moedas distribuídas</p>
        </div>

        {/* Filtros e Resumo */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="md:col-span-2">
            <div className="flex flex-col sm:flex-row gap-4">
              <div className="flex-1">
                <input
                  type="text"
                  placeholder="Buscar por aluno ou descrição..."
                  className="input-field"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
              <div className="w-full sm:w-48">
                <select
                  className="input-field"
                  value={selectedMonth}
                  onChange={(e) => setSelectedMonth(e.target.value)}
                >
                  <option value="">Todos os meses</option>
                  <option value="0">Janeiro</option>
                  <option value="1">Fevereiro</option>
                  <option value="2">Março</option>
                  <option value="3">Abril</option>
                  <option value="4">Maio</option>
                  <option value="5">Junho</option>
                  <option value="6">Julho</option>
                  <option value="7">Agosto</option>
                  <option value="8">Setembro</option>
                  <option value="9">Outubro</option>
                  <option value="10">Novembro</option>
                  <option value="11">Dezembro</option>
                </select>
              </div>
            </div>
          </Card>

          <Card className="bg-gradient-to-br from-coin-green-600 to-coin-green-700">
            <div className="text-white">
              <p className="text-sm font-medium opacity-80">Total Distribuído</p>
              <p className="text-3xl font-bold mt-1">{totalMoedas} moedas</p>
            </div>
          </Card>
        </div>

        {/* Lista de Transações */}
        <Card>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-dark-gray-700">
                  <th className="text-left py-3 px-4 text-dark-gray-300 font-medium">Data</th>
                  <th className="text-left py-3 px-4 text-dark-gray-300 font-medium">Aluno</th>
                  <th className="text-left py-3 px-4 text-dark-gray-300 font-medium">Curso</th>
                  <th className="text-left py-3 px-4 text-dark-gray-300 font-medium">Descrição</th>
                  <th className="text-right py-3 px-4 text-dark-gray-300 font-medium">Valor</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-dark-gray-700">
                {filteredTransactions.map((transaction) => (
                  <tr key={transaction.id} className="hover:bg-dark-gray-700/50">
                    <td className="py-3 px-4 text-dark-gray-200">
                      {new Date(transaction.data).toLocaleDateString('pt-BR')}
                    </td>
                    <td className="py-3 px-4 text-dark-gray-200">{transaction.aluno}</td>
                    <td className="py-3 px-4 text-dark-gray-400">{transaction.curso}</td>
                    <td className="py-3 px-4 text-dark-gray-400">{transaction.descricao}</td>
                    <td className="py-3 px-4 text-right text-coin-green-400 font-medium">
                      {transaction.valor} moedas
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      </div>
    </MainLayout>
  );
};