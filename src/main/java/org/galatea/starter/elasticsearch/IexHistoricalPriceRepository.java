package org.galatea.starter.elasticsearch;

import org.galatea.starter.domain.IexHistoricalPrice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface IexHistoricalPriceRepository extends ElasticsearchRepository<IexHistoricalPrice, String> {

    List<IexHistoricalPrice> findBySymbol(String symbol);
}
