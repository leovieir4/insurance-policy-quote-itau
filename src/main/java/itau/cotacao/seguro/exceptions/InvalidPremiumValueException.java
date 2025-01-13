package itau.cotacao.seguro.exceptions;

public class InvalidPremiumValueException extends RuntimeException {
    public InvalidPremiumValueException(String message) {
        super(message);
    }
}