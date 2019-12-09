package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Client;
import rs.ac.ftn.uns.sep.bank.repository.ClientRepository;
import rs.ac.ftn.uns.sep.bank.service.ClientService;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
