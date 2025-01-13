package itau.cotacao.seguro.handlers;

import itau.cotacao.seguro.controllers.dto.response.ErrorResponse;
import itau.cotacao.seguro.exceptions.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse("ERRO_VALIDACAO", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("PRODUTO_NAO_ENCONTRADO", List.of(e.getMessage())));
    }

    @ExceptionHandler(ProductInactiveException.class)
    public ResponseEntity<ErrorResponse> handleProductInactiveException(ProductInactiveException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("PRODUTO_INATIVO", List.of(e.getMessage())));
    }

    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOfferNotFoundException(OfferNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("OFERTA_NAO_ENCONTRADA", List.of(e.getMessage())));
    }

    @ExceptionHandler(OfferInactiveException.class)
    public ResponseEntity<ErrorResponse> handleOfferInactiveException(OfferInactiveException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("OFERTA_INATIVA", List.of(e.getMessage())));
    }

    @ExceptionHandler(InvalidCoverageException.class)
    public ResponseEntity<ErrorResponse> handleOfferInvalidCoverageException(InvalidCoverageException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("COBERTURAS_INVALIDAS", List.of(e.getMessage())));
    }

    @ExceptionHandler(InvalidCoverageValueException.class)
    public ResponseEntity<ErrorResponse> handleOfferInvalidCoverageValueException(InvalidCoverageValueException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALOR_COBERTURA_INVALIDO", List.of(e.getMessage())));
    }

    @ExceptionHandler(InvalidAssistanceException.class)
    public ResponseEntity<ErrorResponse> handleOfferInvalidAssistanceException(InvalidAssistanceException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("ASSISTENCIAS_INVALIDAS", List.of(e.getMessage())));
    }

    @ExceptionHandler(InvalidPremiumValueException.class)
    public ResponseEntity<ErrorResponse> handleOfferInvalidPremiumValueException(InvalidPremiumValueException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALOR_PREMIO_INVALIDO", List.of(e.getMessage())));
    }

    @ExceptionHandler(InvalidTotalCoverageAmountException.class)
    public ResponseEntity<ErrorResponse> handleOfferInvalidTotalCoverageAmountException(InvalidTotalCoverageAmountException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("VALOR_TOTAL_COBERTURA_INVALIDO", List.of(e.getMessage())));
    }

    @ExceptionHandler(CatalogApiUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleOfferCatalogApiUnavailableException(CatalogApiUnavailableException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse("CATALOGO_INDISPONIVEL", List.of(e.getMessage())));
    }

    @ExceptionHandler(InvalidSendMessageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSendMessageException(InvalidSendMessageException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("ERRO_ENVIAR_MENSAGEM", List.of(e.getMessage())));
    }

    @ExceptionHandler(InsurancePolicyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInsurancePolicyNotFoundException(InsurancePolicyNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("ERRO_CONSULTAR_APOLICI", List.of(e.getMessage())));
    }
}