package itau.cotacao.seguro.usecases.impl;

import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;

import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.exceptions.InsurancePolicyNotFoundException;
import itau.cotacao.seguro.repository.QuoteRepository;
import itau.cotacao.seguro.usecases.GetInsurancePolicyUseCase;
import itau.cotacao.seguro.utils.Messages;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetInsurancePolicyUseCaseImpl implements GetInsurancePolicyUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetInsurancePolicyUseCaseImpl.class);

    private final QuoteRepository quoteRepository;
    private final ModelMapper modelMapper;

    public GetInsurancePolicyUseCaseImpl(QuoteRepository quoteRepository, ModelMapper modelMapper) {
        this.quoteRepository = quoteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public InsurancePolicyResponse getInsurancePolicy(Long idQuery) {
        LOGGER.info("Buscando cotação com idQuery: {}", idQuery);

        QuoteEntity quoteEntity = quoteRepository.findByIdQuery(idQuery);

        if (quoteEntity == null) {
            LOGGER.warn("Cotação não encontrada para idQuery: {}", idQuery);
            throw new InsurancePolicyNotFoundException(String.format(Messages.INSURANCE_POLICE_NOT_FOUND, idQuery));
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(QuoteEntity.class, InsurancePolicyResponse.class)
                .addMapping(QuoteEntity::getIdQuery, InsurancePolicyResponse::setId);

        InsurancePolicyResponse response = modelMapper.map(quoteEntity, InsurancePolicyResponse.class);

        LOGGER.info("Cotação encontrada: {}", response);

        return response;
    }
}