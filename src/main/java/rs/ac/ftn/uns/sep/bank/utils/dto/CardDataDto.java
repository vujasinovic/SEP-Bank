package rs.ac.ftn.uns.sep.bank.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDataDto {
    private String pan;

    private Integer securityCode;

    private String holderName;

    private Date validTo;
}
