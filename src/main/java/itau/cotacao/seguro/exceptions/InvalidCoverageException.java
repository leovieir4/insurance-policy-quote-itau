package itau.cotacao.seguro.exceptions;

public class InvalidCoverageException extends RuntimeException {
    public InvalidCoverageException(String message) {
        super(message);
    }
}