package rs.ac.ftn.uns.sep.bank.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;

    @ManyToOne
    private Account payer;

    @ManyToOne
    private Account recipient;

    private Boolean valid;
}
