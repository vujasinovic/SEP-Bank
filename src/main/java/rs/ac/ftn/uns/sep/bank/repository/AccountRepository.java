package rs.ac.ftn.uns.sep.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.ftn.uns.sep.bank.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
