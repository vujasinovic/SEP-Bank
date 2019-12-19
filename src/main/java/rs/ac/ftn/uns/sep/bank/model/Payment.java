package rs.ac.ftn.uns.sep.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;

    private BigInteger amount;

    @ManyToOne
    private Account account;
}
