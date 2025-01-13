package itau.cotacao.seguro.utils;

public class Messages {

    public static final String PRODUCT_NOT_FOUND = "Produto com id %s não encontrado.";
    public static final String OFFER_NOT_FOUND = "A oferta com id %s não existe para o produto informado.";
    public static final String PRODUCT_INACTIVE = "Produto com id %s inativo";
    public static final String OFFER_INACTIVE = "Oferta com id %s inativo";
    public static final String CATALOG_API_ERROR = "Erro ao consultar a API do Catálogo: %S";
    public static final String INVALID_COVERAGES = "Coberturas inválidas para a oferta.";
    public static final String INVALID_COVERAGE_VALUE = "Valor da cobertura %s excede o limite permitido.";
    public static final String INVALID_ASSISTANCES = "Assistências inválidas para a oferta.";
    public static final String INSURANCE_POLICE_NOT_FOUND = "Apólice com id %s não encotrada";
    public static final String INVALID_PREMIUM_VALUE = "O valor do prêmio mensal deve estar entre %s e %s.";
    public static final String INVALID_TOTAL_COVERAGE_AMOUNT = "O valor total das coberturas não corresponde à soma " +
            "das coberturas informadas.";
    public static final String CATALOG_API_INTERNAL_SERVER_ERRO = "Serviço de catalogo indisponível no momento, tente novamente mais tarde.";
    public static final String SEND_MENSAGE_ERROR = "Erro ao enviar mensagem para a fila de cotação, erro: %s";
}

