package itau.cotacao.seguro.exceptions;

public class InsurancePolicyNotFoundException extends RuntimeException {
    public InsurancePolicyNotFoundException(String message) {
        super(message);
    }
}