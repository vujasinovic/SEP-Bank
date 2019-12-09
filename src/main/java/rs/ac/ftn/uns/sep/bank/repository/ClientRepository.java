package rs.ac.ftn.uns.sep.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.ftn.uns.sep.bank.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
