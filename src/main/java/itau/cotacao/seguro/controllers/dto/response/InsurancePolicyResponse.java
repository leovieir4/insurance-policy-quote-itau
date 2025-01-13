package itau.cotacao.seguro.controllers.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import itau.cotacao.seguro.controllers.dto.request.CustomerRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class InsurancePolicyResponse {
    private Long id;
    private Long insurancePolicyId;
    private UUID productId;
    private UUID offerId;
    private Map<String, BigDecimal> coverages;
    private List<String> assistances;
    private CustomerResponse customer;
    private String category;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
}
