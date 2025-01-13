package itau.cotacao.seguro.repository;

import itau.cotacao.seguro.entities.QuoteEntity;

public interface QuoteRepository {
    QuoteEntity findByIdQuery(Long idQuery);
    void save(QuoteEntity quoteEntity);
}
