package itau.cotacao.seguro.repository.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class QuoteRepositoryImpl implements QuoteRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteRepositoryImpl.class);
    private static final String ID_QUERY_INDEX = "idQuery-index";
    private static final String ID_QUERY_ATTRIBUTE = ":idQuery";
    private static final String ID_QUERY_EXPRESSION = "idQuery = :idQuery";

    private final DynamoDBMapper dynamoDBMapper;

    public QuoteRepositoryImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public QuoteEntity findByIdQuery(Long idQuery) {
        LOGGER.info("Buscando cotação por idQuery: {}", idQuery);

        var expressionAttributeValues = createExpressionAttributeValues(idQuery);
        var queryExpression = createQueryExpression(expressionAttributeValues);

        QuoteEntity quoteEntity = dynamoDBMapper.query(QuoteEntity.class, queryExpression).stream().findFirst().orElse(null);

        if (quoteEntity == null) {
            LOGGER.warn("Cotação não encontrada para idQuery: {}", idQuery);
        } else {
            LOGGER.info("Cotação encontrada para idQuery {}: {}", idQuery, quoteEntity);
        }

        return quoteEntity;
    }

    private Map<String, AttributeValue> createExpressionAttributeValues(Long idQuery) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(ID_QUERY_ATTRIBUTE, new AttributeValue().withN(String.valueOf(idQuery)));
        return expressionAttributeValues;
    }

    private DynamoDBQueryExpression<QuoteEntity> createQueryExpression(Map<String, AttributeValue> expressionAttributeValues) {
        return new DynamoDBQueryExpression<QuoteEntity>()
                .withIndexName(ID_QUERY_INDEX)
                .withKeyConditionExpression(ID_QUERY_EXPRESSION)
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);
    }

    @Override
    public void save(QuoteEntity quoteEntity) {
        LOGGER.info("Salvando cotação: {}", quoteEntity);
        dynamoDBMapper.save(quoteEntity);
    }
}