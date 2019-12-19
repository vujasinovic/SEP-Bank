package rs.ac.ftn.uns.sep.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Card> cards;

    @OneToOne(mappedBy = "account")
    private Client client;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal amount;

    @OneToMany(mappedBy = "account")
    private List<Payment> payments;

}
