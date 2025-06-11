import { useEffect, useState } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import api from '../../services/api';

interface Cupom {
  id: string;
  codigo: string;
  status: string;
  dataResgate: string;
  vantagem?: {
    titulo: string;
    custo: number;
  };
}

export default function CuponsAluno() {
  const [cupons, setCupons] = useState<Cupom[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    async function fetchCupons() {
      try {
        const res = await api.get('/api/transacoes/cupons');
        setCupons(res.data);
      } catch (err) {
        setErro('Erro ao carregar cupons.');
      } finally {
        setLoading(false);
      }
    }
    fetchCupons();
  }, []);

  return (
    <MainLayout>
      <div className="max-w-3xl mx-auto mt-8">
        <h2 className="text-2xl font-bold mb-6 text-dark-gray-100">Meus Cupons Resgatados</h2>
        {loading ? (
          <div className="text-center text-dark-gray-400">Carregando...</div>
        ) : erro ? (
          <div className="text-red-400">{erro}</div>
        ) : cupons.length === 0 ? (
          <div className="text-dark-gray-400">Nenhum cupom resgatado ainda.</div>
        ) : (
          <div className="space-y-4">
            {cupons.map((cupom: Cupom) => (
              <div key={cupom.id} className="bg-dark-gray-800 p-4 rounded-lg border border-dark-gray-700 flex flex-col md:flex-row md:items-center md:justify-between">
                <div>
                  <div className="font-semibold text-dark-gray-100">{cupom.vantagem?.titulo || 'Vantagem'}</div>
                  <div className="text-dark-gray-400 text-sm">Código: <span className="font-mono">{cupom.codigo}</span></div>
                  <div className="text-dark-gray-400 text-sm">Status: {cupom.status}</div>
                  <div className="text-dark-gray-400 text-sm">Resgatado em: {new Date(cupom.dataResgate).toLocaleString()}</div>
                </div>
                <div className="mt-2 md:mt-0">
                  <span className="text-coin-green-400 font-bold">{cupom.vantagem?.custo || '-'} moedas</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </MainLayout>
  );
} 