package itau.cotacao.seguro.api.catalog;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MonthlyPremiumAmount {
    private BigDecimal maxAmount;
    private BigDecimal minAmount;
    private BigDecimal suggestedAmount;
}
