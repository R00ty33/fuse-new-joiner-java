package org.galatea.starter.elasticsearch;

import org.galatea.starter.domain.IexHistoricalPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IexHistoricalPricesServiceImpl implements IexHistoricalPriceService {
    @Autowired
    private IexHistoricalPriceRepository iexHistoricalPriceRepository;
    public IexHistoricalPrice save(IexHistoricalPrice iexHistoricalPrice) {
        return iexHistoricalPriceRepository.save(iexHistoricalPrice);
    }

    public void delete(IexHistoricalPrice iexHistoricalPrice) {
        iexHistoricalPriceRepository.delete(iexHistoricalPrice);
    }

    public Optional<IexHistoricalPrice> findOne(String id) {
        return iexHistoricalPriceRepository.findById(id);
    }

    public Iterable<IexHistoricalPrice> findAll() {
        return iexHistoricalPriceRepository.findAll();
    }

    public List<IexHistoricalPrice> findBySymbol(String symbol) {
        return iexHistoricalPriceRepository.findBySymbol(symbol);
    }
}
