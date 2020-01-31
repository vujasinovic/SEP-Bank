package rs.ac.ftn.uns.sep.bank.utils.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class IssuerRequest {
    private String pan;

    private Integer securityCode;

    private String holderName;

    private Date validTo;

    private String acquirerName;

    private String acquirerOrderId;

    private String acquirerTimestamp;

    private String issuerOrderId;

    private String issuerTimestamp;

    private Boolean successful;
}
