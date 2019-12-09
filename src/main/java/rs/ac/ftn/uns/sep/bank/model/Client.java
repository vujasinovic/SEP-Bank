package rs.ac.ftn.uns.sep.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private Set<Account> accounts;
}
