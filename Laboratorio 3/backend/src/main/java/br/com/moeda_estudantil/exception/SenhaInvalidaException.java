package br.com.moeda_estudantil.exception;

public class SenhaInvalidaException extends RuntimeException {
  public SenhaInvalidaException(String message) {
    super(message);
  }
}