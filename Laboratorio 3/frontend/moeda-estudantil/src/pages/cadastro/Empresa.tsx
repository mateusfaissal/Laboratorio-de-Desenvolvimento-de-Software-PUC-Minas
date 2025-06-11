import { useState } from 'react';
import type { ChangeEvent, FormEvent } from 'react';
import { MainLayout } from '../../components/layout/MainLayout';
import api from '../../services/api';

export default function CadastroEmpresa() {
  const [form, setForm] = useState({
    nome: '',
    email: '',
    senha: '',
    cpf: '',
    cnpj: '',
    descricao: ''
  });
  const [mensagem, setMensagem] = useState('');
  const [loading, setLoading] = useState(false);

  function handleChange(e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setLoading(true);
    setMensagem('');
    try {
      await api.post('/api/empresas/cadastro', form);
      setMensagem('Cadastro realizado com sucesso!');
      setForm({ nome: '', email: '', senha: '', cpf: '', cnpj: '', descricao: '' });
    } catch (err) {
      setMensagem('Erro ao cadastrar empresa.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <MainLayout>
      <div className="max-w-lg mx-auto mt-8 bg-dark-gray-800 p-8 rounded-lg shadow-lg border border-dark-gray-700">
        <h2 className="text-2xl font-bold mb-6 text-dark-gray-100">Cadastro de Empresa Parceira</h2>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <input className="input-field" name="nome" placeholder="Nome da Empresa" value={form.nome} onChange={handleChange} required />
          <input className="input-field" name="email" placeholder="Email" type="email" value={form.email} onChange={handleChange} required />
          <input className="input-field" name="senha" placeholder="Senha" type="password" value={form.senha} onChange={handleChange} required />
          <input className="input-field" name="cpf" placeholder="CPF do responsável" value={form.cpf} onChange={handleChange} required />
          <input className="input-field" name="cnpj" placeholder="CNPJ" value={form.cnpj} onChange={handleChange} required />
          <textarea className="input-field" name="descricao" placeholder="Descrição" value={form.descricao} onChange={handleChange} required />
          <button className="btn btn-primary w-full" type="submit" disabled={loading}>{loading ? 'Cadastrando...' : 'Cadastrar'}</button>
        </form>
        {mensagem && <div className="mt-4 text-center text-coin-green-400">{mensagem}</div>}
      </div>
    </MainLayout>
  );
} 