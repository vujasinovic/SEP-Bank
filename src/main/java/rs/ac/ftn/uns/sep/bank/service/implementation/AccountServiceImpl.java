package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Account;
import rs.ac.ftn.uns.sep.bank.repository.AccountRepository;
import rs.ac.ftn.uns.sep.bank.service.AccountService;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getOne(Long id) {
        return accountRepository.getOne(id);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }


}
