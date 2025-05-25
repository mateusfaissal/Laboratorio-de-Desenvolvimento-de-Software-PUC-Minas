import { useState, useEffect } from 'react';
import type { Vantagem } from '../types/vantagem';
import { vantagemService } from '../services/vantagemService';
import { Card } from './common/Card';

export const ListagemVantagens = () => {
  const [vantagens, setVantagens] = useState<Vantagem[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    carregarVantagens();
  }, []);

  const carregarVantagens = async () => {
    try {
      const data = await vantagemService.listarVantagens();
      setVantagens(data);
      setLoading(false);
    } catch (error) {
      setErro('Erro ao carregar vantagens.');
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-[200px]">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-coin-green-500"></div>
      </div>
    );
  }

  if (erro) {
    return (
      <div className="bg-red-900/20 border border-red-500/50 text-red-400 p-4 rounded-lg">
        {erro}
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {vantagens.map((vantagem) => (
        <Card key={vantagem.id}>
          <div className="aspect-video overflow-hidden rounded-t-lg">
            <img
              src={vantagem.foto}
              alt={vantagem.titulo}
              className="w-full h-full object-cover"
            />
          </div>
          <div className="p-4 space-y-4">
            <div>
              <h3 className="text-xl font-semibold text-dark-gray-100">{vantagem.titulo}</h3>
              <p className="mt-2 text-dark-gray-400">{vantagem.descricao}</p>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-lg font-bold text-coin-green-400">
                {vantagem.custo} moedas
              </span>
              <button
                className="btn btn-primary"
                onClick={() => {/* Implementar resgate de vantagem */}}
              >
                Resgatar
              </button>
            </div>
          </div>
        </Card>
      ))}

      {vantagens.length === 0 && (
        <div className="col-span-full text-center text-dark-gray-400 py-8">
          Nenhuma vantagem disponível no momento.
        </div>
      )}
    </div>
  );
}; 