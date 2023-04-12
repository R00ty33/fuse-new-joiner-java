package org.galatea.starter.entrypoint;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import junitparams.JUnitParamsRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.ASpringTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@RequiredArgsConstructor
@Slf4j
// We need to do a full application start up for this one, since we want the feign clients to be instantiated.
// It's possible we could do a narrower slice of beans, but it wouldn't save that much test run time.
@SpringBootTest
// this gives us the MockMvc variable
@AutoConfigureMockMvc
// we previously used WireMockClassRule for consistency with ASpringTest, but when moving to a dynamic port
// to prevent test failures in concurrent builds, the wiremock server was created too late and feign was
// already expecting it to be running somewhere else, resulting in a connection refused
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock")
// Use this runner since we want to parameterize certain tests.
// See runner's javadoc for more usage.
@RunWith(JUnitParamsRunner.class)
public class IexRestControllerTest extends ASpringTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void testGetSymbolsEndpoint() throws Exception {
    MvcResult result = this.mvc.perform(
        // note that we were are testing the fuse REST end point here, not the IEX end point.
        // the fuse end point in turn calls the IEX end point, which is WireMocked for this test.
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/iex/symbols")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        // some simple validations, in practice I would expect these to be much more comprehensive.
        .andExpect(jsonPath("$[0].symbol", is("A")))
        .andExpect(jsonPath("$[1].symbol", is("AA")))
        .andExpect(jsonPath("$[2].symbol", is("AAAU")))
        .andReturn();
  }

  @Test
  public void testGetLastTradedPrice() throws Exception {
    MvcResult result = this.mvc.perform(
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .get("/iex/lastTradedPrice?symbols=FB")
            // This URL will be hit by the MockMvc client. The result is configured in the file
            // src/test/resources/wiremock/mappings/mapping-lastTradedPrice.json
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].symbol", is("FB")))
        .andExpect(jsonPath("$[0].price").value(new BigDecimal("186.3011")))
        .andReturn();
  }

  @Test
  public void testGetLastTradedPriceEmpty() throws Exception {

    MvcResult result = this.mvc.perform(
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .get("/iex/lastTradedPrice?symbols=")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(Collections.emptyList())))
        .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithDate() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=NET&date=20220202")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0][0].symbol", is("NET")))
            .andExpect(jsonPath("$[0][0].close", is(99.8)))
            .andExpect(jsonPath("$[0][0].high", is(103.245)))
            .andExpect(jsonPath("$[0][0].low", is(97)))
            .andExpect(jsonPath("$[0][0].open", is(103.215)))
            .andExpect(jsonPath("$[0][0].volume", is(7027169)))
            .andExpect(jsonPath("$[0][0].date", is("2022-02-02")))
            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithDate2() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=NET,TSLA&date=20220202")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0][0].symbol", is("NET")))
            .andExpect(jsonPath("$[0][0].close", is(99.8)))
            .andExpect(jsonPath("$[0][0].high", is(103.245)))
            .andExpect(jsonPath("$[0][0].low", is(97)))
            .andExpect(jsonPath("$[0][0].open", is(103.215)))
            .andExpect(jsonPath("$[0][0].volume", is(7027169)))
            .andExpect(jsonPath("$[0][0].date", is("2022-02-02")))
            .andExpect(jsonPath("$[1][0].symbol", is("TSLA")))
            .andExpect(jsonPath("$[1][0].close", is(301.887)))
            .andExpect(jsonPath("$[1][0].high", is(310.5)))
            .andExpect(jsonPath("$[1][0].low", is(296.47)))
            .andExpect(jsonPath("$[1][0].open", is(309.393)))
            .andExpect(jsonPath("$[1][0].volume", is(66793035)))
            .andExpect(jsonPath("$[1][0].date", is("2022-02-02")))
            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithDateInvalidSymbol() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=INVALID&date=20220202")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]", is(Collections.emptyList())))
            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithDateInvalidSymbol2() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=NET,INVALID&date=20220202")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0][0].symbol", is("NET")))
            .andExpect(jsonPath("$[0][0].close", is(99.8)))
            .andExpect(jsonPath("$[0][0].high", is(103.245)))
            .andExpect(jsonPath("$[0][0].low", is(97)))
            .andExpect(jsonPath("$[0][0].open", is(103.215)))
            .andExpect(jsonPath("$[0][0].volume", is(7027169)))
            .andExpect(jsonPath("$[0][0].date", is("2022-02-02")))
            .andExpect(jsonPath("$[1]", is(Collections.emptyList())))
            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithRange() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=NET&range=max")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0][0].symbol", is("NET")))
            .andExpect(jsonPath("$[0][0].open", is(18)))
            .andExpect(jsonPath("$[0][0].close", is(18)))
            .andExpect(jsonPath("$[0][0].date", is("2019-09-13")))
            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithRange2() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=NET,TSLA&range=max")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0][0].symbol", is("NET")))
            .andExpect(jsonPath("$[0][0].open", is(18)))
            .andExpect(jsonPath("$[0][0].close", is(18)))
            .andExpect(jsonPath("$[0][0].date", is("2019-09-13")))
            .andExpect(jsonPath("$[1][0].symbol", is("TSLA")))
            .andExpect(jsonPath("$[1][0].open", is(1.2667)))
            .andExpect(jsonPath("$[1][0].close", is(1.5927)))
            .andExpect(jsonPath("$[1][0].date", is("2010-06-29")))

            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithRangeInvalidSymbol() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=INVALID&range=5y")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]", is(Collections.emptyList())))
            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithRangeInvalidSymbol2() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=TSLA,INVALID&range=max")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0][0].symbol", is("TSLA")))
            .andExpect(jsonPath("$[0][0].open", is(1.2667)))
            .andExpect(jsonPath("$[0][0].close", is(1.5927)))
            .andExpect(jsonPath("$[0][0].date", is("2010-06-29")))
            .andExpect(jsonPath("$[1]", is(Collections.emptyList())))
            .andReturn();
  }

  @Test
  public void testGetHistoricTradedPricesWithEmptyList() throws Exception {
    MvcResult result = this.mvc.perform(
                    org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                            .get("/iex/historicalTradedPrices?symbols=")
                            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(Collections.emptyList())))
            .andReturn();
  }

}
