package itau.cotacao.seguro.exceptions;

public class InvalidCoverageValueException extends RuntimeException {
    public InvalidCoverageValueException(String message) {
        super(message);
    }
}