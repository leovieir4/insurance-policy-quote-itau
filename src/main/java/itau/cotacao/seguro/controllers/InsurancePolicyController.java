package itau.cotacao.seguro.controllers;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyRequest;
import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.usecases.CreateInsurancePolicyUseCase;
import itau.cotacao.seguro.usecases.GetInsurancePolicyUseCase;
import itau.cotacao.seguro.usecases.impl.GetInsurancePolicyUseCaseImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insurance-policies")
public class InsurancePolicyController {


    private final ModelMapper modelMapper;

    private final CreateInsurancePolicyUseCase createInsurancePolicyUseCase;

    private final GetInsurancePolicyUseCase getInsurancePolicyUseCase;

    private final MeterRegistry meterRegistry;

    private static final Logger LOGGER = LoggerFactory.getLogger(InsurancePolicyController.class);

    public InsurancePolicyController(ModelMapper modelMapper,
                                     CreateInsurancePolicyUseCase createInsurancePolicyUseCase,
                                     GetInsurancePolicyUseCase getInsurancePolicyUseCase,
                                     MeterRegistry meterRegistry) {
        this.modelMapper = modelMapper;
        this.createInsurancePolicyUseCase = createInsurancePolicyUseCase;
        this.getInsurancePolicyUseCase = getInsurancePolicyUseCase;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova apólice de seguro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Apólice criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<InsurancePolicyResponse> createInsurancePolicy(@Valid @RequestBody InsurancePolicyRequest request) throws Exception {
        LOGGER.info("Recebida requisição para criar apólice de seguro: {}", request); // Log da requisição

        Timer timer = meterRegistry
                .timer("http.server.requests", "uri", "/insurance-policies", "method", "POST");

        InsurancePolicy insurancePolicy = modelMapper.map(request, InsurancePolicy.class);

        InsurancePolicyResponse insurancePolicyResponse = timer.recordCallable(() ->
                createInsurancePolicyUseCase.create(insurancePolicy));

        LOGGER.info("Apólice de seguro criada com sucesso: {}", insurancePolicyResponse); // Log da resposta

        return new ResponseEntity<>(insurancePolicyResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar uma cotação de seguro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cotação encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = InsurancePolicyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cotação não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<InsurancePolicyResponse> getInsurance(@PathVariable Long id) throws Exception {
        LOGGER.info("Recebida requisição para consultar cotação de seguro com ID: {}", id); // Log da requisição

        Timer timer = meterRegistry
                .timer("http.server.requests", "uri", String.format("/insurance-policies/%s", id),
                        "method", "POST");

        InsurancePolicyResponse insurancePolicyResponse = timer.recordCallable(() ->
                getInsurancePolicyUseCase.getInsurancePolicy(id));

        LOGGER.info("Cotação de seguro encontrada: {}", insurancePolicyResponse); // Log da resposta

        return new ResponseEntity<>(insurancePolicyResponse, HttpStatus.OK);
    }
}