package itau.cotacao.seguro.api.catalog;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OfferResponse {

    private UUID id;
    private UUID productId;
    private String name;
    private LocalDateTime createdAt;
    private boolean active;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private MonthlyPremiumAmount monthlyPremiumAmount;

}
