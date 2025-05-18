import React from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import { Card } from '../../components/common/Card';

// Dados mockados
const MOCK_DATA = {
  saldoMoedas: 180,
  transacoes: [
    {
      id: '1',
      professor: 'Prof. Carlos Eduardo',
      valor: 50,
      data: '2024-03-15',
      descricao: 'Participação ativa em aula'
    },
    {
      id: '2',
      professor: 'Profa. Ana Beatriz',
      valor: 30,
      data: '2024-03-10',
      descricao: 'Ajuda aos colegas durante o projeto'
    },
    {
      id: '3',
      professor: 'Prof. Ricardo Silva',
      valor: 40,
      data: '2024-03-05',
      descricao: 'Excelente apresentação do trabalho'
    }
  ],
  vantagens: [
    {
      id: '1',
      nome: 'Desconto na Cantina',
      custo: 50,
      descricao: '15% de desconto em qualquer refeição'
    },
    {
      id: '2',
      nome: 'Certificado Extra',
      custo: 100,
      descricao: 'Certificado de horas complementares'
    },
    {
      id: '3',
      nome: 'Vale Livro',
      custo: 150,
      descricao: 'R$ 50,00 em compras na livraria parceira'
    }
  ]
};

export const Dashboard: React.FC = () => {
  return (
    <MainLayout>
      <div className="space-y-6">
        {/* Cabeçalho */}
        <div>
          <h1 className="text-2xl font-bold text-dark-gray-100">Dashboard do Aluno</h1>
          <p className="mt-1 text-dark-gray-400">Acompanhe suas moedas e troque por vantagens</p>
        </div>

        {/* Saldo */}
        <Card className="bg-gradient-to-br from-coin-green-600 to-coin-green-700">
          <div className="text-white">
            <p className="text-sm font-medium opacity-80">Seu Saldo</p>
            <p className="text-4xl font-bold mt-2">{MOCK_DATA.saldoMoedas} moedas</p>
          </div>
        </Card>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Histórico de Moedas */}
          <Card title="Histórico de Moedas Recebidas">
            <div className="divide-y divide-dark-gray-700">
              {MOCK_DATA.transacoes.map((transacao) => (
                <div key={transacao.id} className="py-4 first:pt-0 last:pb-0">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium text-dark-gray-200">{transacao.professor}</p>
                      <p className="text-sm text-dark-gray-400 mt-1">{transacao.descricao}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-coin-green-400 font-medium">
                        +{transacao.valor} moedas
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

          {/* Vantagens Disponíveis */}
          <Card title="Vantagens Disponíveis">
            <div className="divide-y divide-dark-gray-700">
              {MOCK_DATA.vantagens.map((vantagem) => (
                <div key={vantagem.id} className="py-4 first:pt-0 last:pb-0">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium text-dark-gray-200">{vantagem.nome}</p>
                      <p className="text-sm text-dark-gray-400 mt-1">{vantagem.descricao}</p>
                    </div>
                    <button
                      className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors duration-200 ${
                        MOCK_DATA.saldoMoedas >= vantagem.custo
                          ? 'bg-coin-green-600 text-white hover:bg-coin-green-700'
                          : 'bg-dark-gray-700 text-dark-gray-400 cursor-not-allowed'
                      }`}
                      disabled={MOCK_DATA.saldoMoedas < vantagem.custo}
                    >
                      Trocar ({vantagem.custo} moedas)
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>
      </div>
    </MainLayout>
  );
};