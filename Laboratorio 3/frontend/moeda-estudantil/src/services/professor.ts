import type { Professor, SendCoinFormData, Student, Transaction } from '../types';
import api from './api';

export async function getProfessorProfile(): Promise<Professor> {
  const response = await api.get<Professor>('/professors/profile');
  return response.data;
}

export async function getProfessorBalance(): Promise<number> {
  const response = await api.get<{ balance: number }>('/professors/balance');
  return response.data.balance;
}

export async function getProfessorTransactions(): Promise<Transaction[]> {
  const response = await api.get<Transaction[]>('/professors/transactions');
  return response.data;
}

export async function sendCoins(data: SendCoinFormData): Promise<Transaction> {
  const response = await api.post<Transaction>('/professors/send-coins', data);
  return response.data;
}

export async function getStudentList(): Promise<Student[]> {
  const response = await api.get<Student[]>('/professors/students');
  return response.data;
}