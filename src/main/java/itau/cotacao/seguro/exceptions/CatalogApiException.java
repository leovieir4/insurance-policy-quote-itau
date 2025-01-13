package itau.cotacao.seguro.exceptions;

public class CatalogApiException extends RuntimeException {
    public CatalogApiException(String message) {
        super(message);
    }
}