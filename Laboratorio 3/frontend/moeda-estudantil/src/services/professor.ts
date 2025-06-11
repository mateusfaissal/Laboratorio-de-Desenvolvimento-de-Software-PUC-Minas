import type { Professor, SendCoinFormData, Student, Transaction } from '../types';
import api from './api';

export async function getProfessorProfile(): Promise<Professor> {
  const response = await api.get<Professor>('/api/professores/me');
  return response.data;
}

export async function getProfessorBalance(): Promise<number> {
  const response = await api.get<number>('/api/professores/saldo');
  return response.data;
}

export async function getProfessorTransactions(): Promise<Transaction[]> {
  const response = await api.get<Transaction[]>('/api/transacoes/extrato');
  return response.data;
}

export async function sendCoins(data: SendCoinFormData): Promise<Transaction> {
  const response = await api.post<Transaction>('/api/professores/enviar-moedas', data);
  return response.data;
}

export async function getStudentList(): Promise<Student[]> {
  const response = await api.get<Student[]>('/api/alunos');
  return response.data;
}