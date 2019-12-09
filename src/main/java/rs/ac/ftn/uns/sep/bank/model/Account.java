package rs.ac.ftn.uns.sep.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "account")
    private Set<Card> cards;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    private Client client;
}
