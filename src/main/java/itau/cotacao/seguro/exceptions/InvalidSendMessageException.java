package itau.cotacao.seguro.exceptions;

public class InvalidSendMessageException extends RuntimeException {
    public InvalidSendMessageException(String message) {
        super(message);
    }
}