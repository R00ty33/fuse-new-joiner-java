package org.galatea.starter.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexService {

  @NonNull
  private IexClient iexClient;
  @NonNull
  private IexCloudClient iexCloudClient;



  /**
   * Get all stock symbols from IEX.
   *
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols() {
    return iexClient.getAllSymbols();
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]));
    }
  }

  /**
   * Get the historical traded prices for a given range or date.
   *
   * @param symbol the ticker for the stock
   * @param range the time series for the historical traded price (max,5y,2y,1y,ytd,6m,3m,1m,5d)
   * @param date the specific date (YYYYMMDD)
   * @return A list of historical traded price objects for each Symbol that is passed in
   */
  public List<IexHistoricalPrice> getHistoricalPriceForSymbol(
          final String symbol, final String range, final String date) {
    if (StringUtils.isBlank(symbol)) {
      return Collections.emptyList();
    } else {
      if (StringUtils.isNotBlank(range)) {
        return iexCloudClient.getHistoricalPricesForSymbolWithRange(symbol, range);
      } else if (StringUtils.isNotBlank(date)) {
        return iexCloudClient.getHistoricalPricesForSymbolWithDate(symbol, date);
      } else {
        return iexCloudClient.getHistoricalPricesForSymbolWithRange(symbol, "max");
      }
    }
  }
}
