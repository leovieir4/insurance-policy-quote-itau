package itau.cotacao.seguro.exceptions;

public class InvalidAssistanceException extends RuntimeException {
    public InvalidAssistanceException(String message) {
        super(message);
    }
}