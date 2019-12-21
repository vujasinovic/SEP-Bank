package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Transaction;
import rs.ac.ftn.uns.sep.bank.repository.TransactionRepository;
import rs.ac.ftn.uns.sep.bank.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
