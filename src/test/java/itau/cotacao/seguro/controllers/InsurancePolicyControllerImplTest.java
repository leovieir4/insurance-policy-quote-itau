package itau.cotacao.seguro.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import itau.cotacao.seguro.controllers.dto.request.CustomerRequest;
import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyRequest;
import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.exceptions.InsurancePolicyNotFoundException;
import itau.cotacao.seguro.usecases.CreateInsurancePolicyUseCase;
import itau.cotacao.seguro.usecases.GetInsurancePolicyUseCase;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = InsurancePolicyController.class)
class InsurancePolicyControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateInsurancePolicyUseCase createInsurancePolicyUseCase;

    @MockBean
    private GetInsurancePolicyUseCase getInsurancePolicyUseCase;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private MeterRegistry meterRegistry;

    @Test
    void createInsurancePolicy_shouldReturnCreated() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest();

        request.setAssistances(List.of("teste"));
        request.setOfferId(UUID.randomUUID());
        request.setProductId(UUID.randomUUID());
        request.setCustomer(new CustomerRequest());

        Map<String, BigDecimal> cove = new HashMap<>();
        cove.put("teste", BigDecimal.valueOf(10));
        request.setCoverages(cove);

        InsurancePolicy insurancePolicy = new InsurancePolicy(); // Crie uma instância de InsurancePolicy
        InsurancePolicyResponse response = new InsurancePolicyResponse(); // Crie uma instância de InsurancePolicyResponse

        when(modelMapper.map(request, InsurancePolicy.class)).thenReturn(insurancePolicy);
        when(createInsurancePolicyUseCase.create(insurancePolicy)).thenReturn(response);

        Timer timer = mock(Timer.class);
        when(meterRegistry.timer("http.server.requests", "uri", "/insurance-policies", "method", "POST"))
                .thenAnswer(invocation -> timer);
        when(timer.recordCallable(any(Callable.class))).thenAnswer(invocation -> {
            Callable<InsurancePolicyResponse> callable = invocation.getArgument(0);
            return callable.call(); // Executa a Callable e retorna o resultado
        });

        mockMvc.perform(post("/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(asJsonString(response)));
    }

    @Test
    void createInsurancePolicy_invalidRequest_shouldReturnBadRequest() throws Exception {
        InsurancePolicyRequest request = new InsurancePolicyRequest(); // Crie uma instância inválida de InsurancePolicyRequest

        mockMvc.perform(post("/insurance-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCotacao_shouldReturnOk() throws Exception {
        Long id = 1L;
        InsurancePolicyResponse response = new InsurancePolicyResponse(); // Crie uma instância de InsurancePolicyResponse

        when(getInsurancePolicyUseCase.getInsurancePolicy(id)).thenReturn(response);

        Timer timer = mock(Timer.class);
        when(meterRegistry.timer("http.server.requests", "uri", "/insurance-policies/1", "method", "POST"))
                .thenAnswer(invocation -> timer);
        when(timer.recordCallable(any(Callable.class))).thenAnswer(invocation -> {
            Callable<InsurancePolicyResponse> callable = invocation.getArgument(0);
            return callable.call(); // Executa a Callable e retorna o resultado
        });

        mockMvc.perform(get("/insurance-policies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(response)));
    }

    @Test
    void getCotacao_notFound_shouldReturnNotFound() throws Exception {
        Long id = 1L;

        when(getInsurancePolicyUseCase.getInsurancePolicy(id)).thenThrow(new InsurancePolicyNotFoundException("Cotação não encontrada"));
        Timer timer = mock(Timer.class);
        when(meterRegistry.timer("http.server.requests", "uri", "/insurance-policies/1", "method", "POST"))
                .thenAnswer(invocation -> timer);
        when(timer.recordCallable(any(Callable.class))).thenAnswer(invocation -> {
            Callable<InsurancePolicyResponse> callable = invocation.getArgument(0);
            return callable.call(); // Executa a Callable e retorna o resultado
        });
        mockMvc.perform(get("/insurance-policies/{id}", id))
                .andExpect(status().isNotFound());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}