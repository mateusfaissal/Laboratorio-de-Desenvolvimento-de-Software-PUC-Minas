import { useState } from 'react';
import type { FormEvent } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import api from '../../services/api';

export default function ValidarCupomEmpresa() {
  const [cupomId, setCupomId] = useState('');
  const [codigoConfirmacao, setCodigoConfirmacao] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setMensagem('');
    setLoading(true);
    try {
      await api.post(`/api/transacoes/cupons/${cupomId}/validar`, { codigoConfirmacao });
      setMensagem('Cupom validado com sucesso!');
    } catch (err) {
      setMensagem('Erro ao validar cupom.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <MainLayout>
      <div className="max-w-lg mx-auto mt-8 bg-dark-gray-800 p-8 rounded-lg shadow-lg border border-dark-gray-700">
        <h2 className="text-2xl font-bold mb-6 text-dark-gray-100">Validação de Cupom</h2>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <input className="input-field" placeholder="ID do Cupom" value={cupomId} onChange={e => setCupomId(e.target.value)} required />
          <input className="input-field" placeholder="Código de Confirmação" value={codigoConfirmacao} onChange={e => setCodigoConfirmacao(e.target.value)} required />
          <button className="btn btn-primary w-full" type="submit" disabled={loading}>{loading ? 'Validando...' : 'Validar Cupom'}</button>
        </form>
        {mensagem && <div className="mt-4 text-center text-coin-green-400">{mensagem}</div>}
      </div>
    </MainLayout>
  );
} 