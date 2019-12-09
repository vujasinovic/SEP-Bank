package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Card;
import rs.ac.ftn.uns.sep.bank.repository.CardRepository;
import rs.ac.ftn.uns.sep.bank.service.CardService;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
