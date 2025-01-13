package itau.cotacao.seguro.usecases.impl;

import itau.cotacao.seguro.controllers.dto.request.InsurancePolicyListenerRequest;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.repository.QuoteRepository;
import itau.cotacao.seguro.usecases.ProcessInsurancePolicyResponseUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessInsurancePolicyResponseUseCaseImpl implements ProcessInsurancePolicyResponseUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessInsurancePolicyResponseUseCaseImpl.class);

    private final QuoteRepository quoteRepository;

    public ProcessInsurancePolicyResponseUseCaseImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public void process(InsurancePolicyListenerRequest request) {
        try {
            QuoteEntity quoteEntity = quoteRepository.findByIdQuery(Long.parseLong(request.getId()));

            if (quoteEntity == null) {
                LOGGER.warn("Cotação não encontrada para idQuery: {}", request.getId());
                return;
            }

            quoteEntity.setInsurancePolicyId(request.getInsurancePolicyId());
            quoteRepository.save(quoteEntity);

            LOGGER.info("Cotação atualizada com o número da apólice: {}", quoteEntity);
        } catch (Exception e) {
            LOGGER.error("Erro ao processar InsurancePolicyResponse", e);
        }
    }
}