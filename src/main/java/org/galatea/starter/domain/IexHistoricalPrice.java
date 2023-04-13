package org.galatea.starter.domain;

import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "historical_prices")
public class IexHistoricalPrice {
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;
  @Id
  private String symbol;
  private Integer volume;
  private String date;
}
