package itau.cotacao.seguro.usecases.impl;

import itau.cotacao.seguro.api.catalog.OfferResponse;
import itau.cotacao.seguro.api.catalog.ProductResponse;
import itau.cotacao.seguro.controllers.dto.response.InsurancePolicyResponse;
import itau.cotacao.seguro.domain.model.InsurancePolicy;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.repository.QuoteRepository;
import itau.cotacao.seguro.services.CatalogService;
import itau.cotacao.seguro.services.InsurancePolicyProducer;
import itau.cotacao.seguro.usecases.CreateInsurancePolicyUseCase;
import itau.cotacao.seguro.validators.impl.InsurancePolicyValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CreateInsurancePolicyUseCaseImpl implements CreateInsurancePolicyUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInsurancePolicyUseCaseImpl.class);

    private static final int MIN_FIVE_DIGIT_ID = 10000;
    private static final int MAX_FIVE_DIGIT_ID = 99999;

    private final CatalogService catalogService;
    private final InsurancePolicyValidator validator;
    private final QuoteRepository quoteRepository;
    private final InsurancePolicyProducer insurancePolicyProducer;
    private final ModelMapper modelMapper;

    public CreateInsurancePolicyUseCaseImpl(CatalogService catalogService, InsurancePolicyValidator validator,
                                            QuoteRepository quoteRepository, InsurancePolicyProducer insurancePolicyProducer,
                                            ModelMapper modelMapper) {
        this.catalogService = catalogService;
        this.validator = validator;
        this.quoteRepository = quoteRepository;
        this.insurancePolicyProducer = insurancePolicyProducer;
        this.modelMapper = modelMapper;
    }

    @Override
    public InsurancePolicyResponse create(InsurancePolicy insurancePolicy) {
        try {
            ProductResponse product = catalogService.getProduct(insurancePolicy.getProductId());
            OfferResponse offer = catalogService.getOffer(insurancePolicy.getOfferId());

            validator.validate(insurancePolicy, product, offer);

            QuoteEntity quoteEntity = mapToQuoteEntity(insurancePolicy);
            quoteEntity.setIdQuery(generateFiveDigitId());

            quoteRepository.save(quoteEntity);

            InsurancePolicyResponse insurancePolicyResponse = createInsurancePolicyResponse(quoteEntity);

            insurancePolicyProducer.send(insurancePolicyResponse);

            return insurancePolicyResponse;
        } catch (Exception e) {
            LOGGER.error("Erro ao criar apólice de seguro", e);
            throw e;
        }
    }

    private InsurancePolicyResponse createInsurancePolicyResponse(QuoteEntity quoteEntity) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<QuoteEntity, InsurancePolicyResponse> typeMap = modelMapper.typeMap(QuoteEntity.class, InsurancePolicyResponse.class);
        typeMap.addMapping(QuoteEntity::getCustomer, InsurancePolicyResponse::setCustomer); // Corrigido para getCustomerEntity
        typeMap.addMapping(QuoteEntity::getIdQuery, InsurancePolicyResponse::setId); // Corrigido para setQuoteId

        return modelMapper.map(quoteEntity, InsurancePolicyResponse.class);
    }

    private QuoteEntity mapToQuoteEntity(InsurancePolicy insurancePolicy) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.typeMap(InsurancePolicy.class, QuoteEntity.class)
                .addMapping(InsurancePolicy::getCustomer, QuoteEntity::setCustomer);

        return modelMapper.map(insurancePolicy, QuoteEntity.class);
    }

    private Long generateFiveDigitId() {
        Random random = new Random();
        return (long) (random.nextInt(MAX_FIVE_DIGIT_ID - MIN_FIVE_DIGIT_ID + 1) + MIN_FIVE_DIGIT_ID);
    }
}