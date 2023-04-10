package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.service.IexService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@Validated
@RestController
@RequiredArgsConstructor
public class IexRestController {

  @NonNull
  private IexService iexService;

  /**
   * Exposes an endpoint to get all of the symbols available on IEX.
   *
   * @return a list of all IexStockSymbols.
   */
  @GetMapping(value = "${mvc.iex.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexSymbol> getAllStockSymbols() {
    return iexService.getAllSymbols();
  }

  /**
   * Get the last traded price for each of the symbols passed in.
   *
   * @param symbols list of symbols to get last traded price for.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.iex.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbols") final List<String> symbols) {
    return iexService.getLastTradedPriceForSymbols(symbols);
  }

  /**
   * Get the historical traded prices for a given range or date.
   *
   * @param symbols the ticker for the stock
   * @param ranges the time series for the historical traded price (max,5y,2y,1y,ytd,6m,3m,1m,5d)
   * @param date the specific date (YYYYMMDD)
   * @return A list of historical traded price objects for each Symbol that is passed in
   */
  @GetMapping(value = "${mvc.iex.getHistoricalPricePath}", produces = {
        MediaType.APPLICATION_JSON_VALUE})
  public List<List<IexHistoricalPrice>> getHistoricalTradedPrices(
        @RequestParam(value = "symbols", required = true) final List<String> symbols,
        @RequestParam(value = "ranges", required = false) final List<String> ranges,
        @RequestParam(value = "date", required = false) final String date) {
    return iexService.getHistoricalPriceForSymbol(symbols, ranges, date);
  }
}
