package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.ftn.uns.sep.bank.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAll();

    Client findByMerchantId(String merchantId);
}
