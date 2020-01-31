package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.utils.dto.AcquirerRequest;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.IssuerRequest;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment findByUrl(String url);

    String submitCardData(CardDataDto cardDataDto, String url);

    IssuerRequest handleAcquirerRequest(AcquirerRequest acquirerRequest);
}
