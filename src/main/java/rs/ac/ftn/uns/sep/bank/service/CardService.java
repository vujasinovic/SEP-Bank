package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.ftn.uns.sep.bank.model.Card;

import java.util.List;

public interface CardService {
    List<Card> findAll();

    Card findByPan(String pan);
}
