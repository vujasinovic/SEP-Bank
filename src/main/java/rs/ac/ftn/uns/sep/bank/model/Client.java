package rs.ac.ftn.uns.sep.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "client")
    private Set<Account> accounts;
}
