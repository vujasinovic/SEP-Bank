package rs.ac.ftn.uns.sep.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String pan;

    private Integer securityCode;

    private String holderName;

    private Date validTo;

    @ManyToOne
    @JoinColumn(name = "account", nullable = false)
    private Account account;
}
