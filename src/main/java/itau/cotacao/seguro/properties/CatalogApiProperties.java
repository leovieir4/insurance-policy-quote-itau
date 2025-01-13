package itau.cotacao.seguro.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.integration.catalog")
@Getter
@Setter
public class CatalogApiProperties {

    private String host;
    private Services services;

    @Getter
    @Setter
    public static class Services {
        private String getProducts;
        private String getOffers; // Nova propriedade para o endpoint de ofertas
    }
}