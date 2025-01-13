package itau.cotacao.seguro.exceptions;

public class CatalogApiUnavailableException extends RuntimeException {
    public CatalogApiUnavailableException(String message) {
        super(message);
    }
}