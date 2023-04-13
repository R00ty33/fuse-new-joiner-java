package org.galatea.starter.elasticsearch;

import org.galatea.starter.domain.IexHistoricalPrice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IexHistoricalPriceService {

    IexHistoricalPrice save(IexHistoricalPrice iexHistoricalPrice);

    void delete(IexHistoricalPrice iexHistoricalPrice);

    Optional<IexHistoricalPrice> findOne(String id);

    Iterable<IexHistoricalPrice> findAll();

    List<IexHistoricalPrice> findBySymbol(String symbol);
}
