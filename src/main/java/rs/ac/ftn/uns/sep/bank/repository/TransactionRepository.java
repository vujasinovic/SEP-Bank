package rs.ac.ftn.uns.sep.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.ftn.uns.sep.bank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
