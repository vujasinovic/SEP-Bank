package rs.ac.ftn.uns.sep.bank.utils.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class AcquirerRequest {
    private String pan;

    private Integer securityCode;

    private String holderName;

    private Date validTo;

    private BigDecimal amount;

    private String acquirerName;

    private String acquirerOrderId;

    private String acquirerTimestamp;
}
