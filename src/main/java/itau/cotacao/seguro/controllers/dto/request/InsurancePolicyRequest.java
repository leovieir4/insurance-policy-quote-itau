package itau.cotacao.seguro.controllers.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
public class InsurancePolicyRequest {

    @NotNull(message = "O ID do produto é obrigatório.")
    @Schema(defaultValue = "1b2da7cc-b367-4196-8a78-9cfeec21f587")
    private UUID productId;

    @NotNull(message = "O ID da oferta é obrigatório.")
    @Schema(defaultValue = "adc56d77-348c-4bf0-908f-22d402ee715c")
    private UUID offerId;

    @NotEmpty(message = "As coberturas são obrigatórias.")
    private Map<String, BigDecimal> coverages;

    @NotEmpty(message = "As assistências são obrigatórias.")
    private List<String> assistances;

    @NotNull(message = "Os dados do cliente são obrigatórios.")
    private CustomerRequest customer;

    private String category;
    private BigDecimal totalMonthlyPremiumAmount;
    private BigDecimal totalCoverageAmount;
}
