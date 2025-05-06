package br.com.moeda_estudantil.exception;

public class RecursoJaExisteException extends RuntimeException {
  public RecursoJaExisteException(String message) {
    super(message);
  }
}
