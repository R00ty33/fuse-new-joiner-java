package org.galatea.starter.domain;

import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "historical_prices", type = "IexHistoricalPrice")
public class IexHistoricalPrice {
  @Id
  private String id;
  private String symbol;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;
  private Integer volume;
  private String date;
}
