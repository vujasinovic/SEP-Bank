package rs.ac.ftn.uns.sep.bank.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDto {
    private Long id;

    private String url;
}
