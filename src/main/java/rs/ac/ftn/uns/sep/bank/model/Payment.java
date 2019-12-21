package rs.ac.ftn.uns.sep.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;

    private BigDecimal amount;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Account merchant;

    private String successUrl;

    private String failedUrl;

    private String errorUrl;

}
