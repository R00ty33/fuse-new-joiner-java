package org.galatea.starter.service;

import java.util.List;

import org.galatea.starter.domain.IexHistoricalPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * A Feign Declarative REST Client to access endpoints from the Free and Open IEX API to get market
 * data. See https://iextrading.com/developer/docs/
 */

@FeignClient(name = "IEX", url = "${spring.rest.iexCloudPath}")
public interface IexCloudClient {

  /**
  * Get the historical traded prices for a given range.
  *
  * @param symbol the ticker for the stock
  * @param range the time series for the historical traded price (max,5y,2y,1y,ytd,6m,3m,1m,5d)
  * @return A list of historical traded price objects for each Symbol that is passed in
  */
  @GetMapping("/stock/{symbol}/chart/{range}?token=${spring.rest.iexAuthToken}")
    List<IexHistoricalPrice> getHistoricalPricesForSymbolWithRange(
            @PathVariable(value = "symbol") String symbol,
            @PathVariable(value = "range") String range);

  /**
   * Get the historical traded prices for a given date.
   *
   * @param symbol the ticker for the stock
   * @param date the specific date (YYYYMMDD)
   * @return A list of historical traded price objects for each Symbol that is passed in
   */
  @GetMapping("/stock/{symbol}/chart/date/{date}?chartByDay=true&token=${spring.rest.iexAuthToken}")
  List<IexHistoricalPrice> getHistoricalPricesForSymbolWithDate(
        @PathVariable(value = "symbol") String symbol,
        @PathVariable(value = "date") String date);
}
