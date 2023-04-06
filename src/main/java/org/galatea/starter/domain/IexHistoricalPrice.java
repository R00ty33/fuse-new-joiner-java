package org.galatea.starter.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class IexHistoricalPrice {
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private String symbol;
    private Integer volume;
    private Date date;

}
