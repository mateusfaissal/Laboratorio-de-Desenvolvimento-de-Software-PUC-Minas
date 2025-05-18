import React from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';

// Dados mockados
const MOCK_DATA = {
  saldoMoedas: 1000,
  ultimasTransacoes: [
    {
      id: '1',
      aluno: 'Maria Silva',
      valor: 50,
      data: '2024-03-15',
      descricao: 'Participação ativa em aula'
    },
    {
      id: '2',
      aluno: 'João Santos',
      valor: 30,
      data: '2024-03-14',
      descricao: 'Ajuda aos colegas'
    },
    {
      id: '3',
      aluno: 'Ana Oliveira',
      valor: 40,
      data: '2024-03-13',
      descricao: 'Excelente apresentação'
    }
  ],
  estatisticas: {
    totalAlunos: 45,
    moedasDistribuidas: 500,
    mediaAluno: 11.1
  }
};

export const Dashboard: React.FC = () => {
  return (
    <MainLayout>
      <div className="space-y-6">
        {/* Cabeçalho */}
        <div>
          <h1 className="text-2xl font-bold text-dark-gray-100">Dashboard do Professor</h1>
          <p className="mt-1 text-dark-gray-400">Gerencie suas moedas e acompanhe as distribuições</p>
        </div>

        {/* Grid de Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="bg-gradient-to-br from-coin-green-600 to-coin-green-700">
            <div className="text-white">
              <p className="text-sm font-medium opacity-80">Saldo Disponível</p>
              <p className="text-3xl font-bold mt-1">{MOCK_DATA.saldoMoedas} moedas</p>
            </div>
          </Card>

          <Card>
            <div>
              <p className="text-sm font-medium text-dark-gray-400">Total de Alunos</p>
              <p className="text-2xl font-bold text-dark-gray-100 mt-1">
                {MOCK_DATA.estatisticas.totalAlunos}
              </p>
            </div>
          </Card>

          <Card>
            <div>
              <p className="text-sm font-medium text-dark-gray-400">Moedas Distribuídas</p>
              <p className="text-2xl font-bold text-dark-gray-100 mt-1">
                {MOCK_DATA.estatisticas.moedasDistribuidas}
              </p>
            </div>
          </Card>
        </div>

        {/* Últimas Transações */}
        <Card title="Últimas Transações">
          <div className="divide-y divide-dark-gray-700">
            {MOCK_DATA.ultimasTransacoes.map((transacao) => (
              <div key={transacao.id} className="py-4 first:pt-0 last:pb-0">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium text-dark-gray-200">{transacao.aluno}</p>
                    <p className="text-sm text-dark-gray-400 mt-1">{transacao.descricao}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-coin-green-400 font-medium">
                      {transacao.valor} moedas
                    </p>
                    <p className="text-sm text-dark-gray-400 mt-1">
                      {new Date(transacao.data).toLocaleDateString('pt-BR')}
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </MainLayout>
  );
};