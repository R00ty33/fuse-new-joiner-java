package org.galatea.starter.service;

import java.util.ArrayList;
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
   * @param symbols the ticker for the stock
   * @param range the time series for the historical traded price (max,5y,2y,1y,ytd,6m,3m,1m,5d)
   * @param date the specific date (YYYYMMDD)
   * @return A list of historical traded price objects for each Symbol that is passed in
   */
  public List<List<IexHistoricalPrice>> getHistoricalPriceForSymbol(
          final List<String> symbols, final String range, final String date) {
    if (CollectionUtils.isEmpty(symbols)) { // symbols must not be empty
      log.warn("User passed empty list to /HistorcalTradePrices endpoint. "
              + "Symbols must not be empty.");
      return Collections.emptyList();
    } else {
      List<List<IexHistoricalPrice>> historicalPriceListForSymbols = new ArrayList<>();
      List<IexHistoricalPrice> iexHistoricalPrice = new ArrayList<>();
      for (String symbol : symbols) {
        iexHistoricalPrice = getHistoricalPricesForSymbol(symbol, range, date);
        if (!CollectionUtils.isEmpty(iexHistoricalPrice)) {
          historicalPriceListForSymbols.add(iexHistoricalPrice);
        }
      }
      return historicalPriceListForSymbols;
    }
  }

  public List<IexHistoricalPrice> getHistoricalPricesForSymbol(String symbol, String range, String date) {
    List<IexHistoricalPrice> result = new ArrayList<>();
    try {
      if (StringUtils.isNotBlank(date)) {
        result = iexCloudClient
                .getHistoricalPricesForSymbolWithDate(symbol, date);
      }
      else {
        if (StringUtils.isBlank(range)) {
          range = "max";
        }
        result = iexCloudClient
                .getHistoricalPricesForSymbolWithRange(symbol, range);
      }
    } catch (Exception e) {
      log.error("Exception: " + e
              + " occurred when calling downstream IEX /historicalPrices API passing symbol: "
              + symbol);
    }
    return result;
  }
}
