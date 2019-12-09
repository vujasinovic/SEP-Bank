package rs.ac.ftn.uns.sep.bank.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class KpDto {
    @Max(value = 30)
    private String merchantId;

    @Max(value = 100)
    private String merchantPassword;

    private Double amount;

    private Integer merchantOrderId;

    private LocalDateTime merchantTimestamp;

    private String successUrl;

    private String failedUrl;

    private String errorUrl;
}
