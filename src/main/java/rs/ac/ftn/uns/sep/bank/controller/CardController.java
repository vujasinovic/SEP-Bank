package rs.ac.ftn.uns.sep.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.ftn.uns.sep.bank.model.Card;
import rs.ac.ftn.uns.sep.bank.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<Card> getAll() {
        return cardService.findAll();
    }
}
