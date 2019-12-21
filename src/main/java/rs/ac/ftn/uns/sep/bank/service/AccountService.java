package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.ftn.uns.sep.bank.model.Account;

import java.util.List;

public interface AccountService {
    Account getOne(Long id);

    List<Account> findAll();

    Account save(Account account);
}
