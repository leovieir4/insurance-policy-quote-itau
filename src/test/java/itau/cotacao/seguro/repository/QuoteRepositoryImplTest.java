package itau.cotacao.seguro.repository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import itau.cotacao.seguro.entities.QuoteEntity;
import itau.cotacao.seguro.repository.impl.QuoteRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteRepositoryImplTest {

    private static final String ID_QUERY_INDEX = "idQuery-index";
    private static final Long ID_QUERY = 123L;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private QuoteRepositoryImpl quoteRepository;

    @Test
    void findByIdQuery_shouldReturnQuoteEntity() {
        QuoteEntity expectedQuote = new QuoteEntity();

        PaginatedQueryList<QuoteEntity> paginatedList = mock(PaginatedQueryList.class);
        when(paginatedList.stream()).thenReturn(Stream.of(expectedQuote));

        when(dynamoDBMapper.query(eq(QuoteEntity.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(paginatedList);

        QuoteEntity actualQuote = quoteRepository.findByIdQuery(ID_QUERY);

        assertEquals(expectedQuote, actualQuote);
    }

    @Test
    void findByIdQuery_noQuoteFound_shouldReturnNull() {

        PaginatedQueryList<QuoteEntity> paginatedList = mock(PaginatedQueryList.class);
        when(paginatedList.stream()).thenReturn(Stream.empty()); // Retornar um stream vazio

        when(dynamoDBMapper.query(eq(QuoteEntity.class), any(DynamoDBQueryExpression.class)))
                .thenReturn(paginatedList);

        QuoteEntity actualQuote = quoteRepository.findByIdQuery(ID_QUERY);

        assertNull(actualQuote);
    }

    @Test
    void save_shouldCallDynamoDBMapperSave() {
        QuoteEntity quoteEntity = new QuoteEntity();

        quoteRepository.save(quoteEntity);

        verify(dynamoDBMapper).save(quoteEntity);
    }
}