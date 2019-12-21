package rs.ac.ftn.uns.sep.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import java.util.Set;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @Size(max = 30)
    private String merchantId;

    @Size(max = 100)
    private String merchantPassword;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id", unique = true)
    private Account account;

}
