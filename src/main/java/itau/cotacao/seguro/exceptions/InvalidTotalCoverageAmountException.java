package itau.cotacao.seguro.exceptions;

public class InvalidTotalCoverageAmountException extends RuntimeException {
    public InvalidTotalCoverageAmountException(String message) {
        super(message);
    }
}