package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentResponse;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentRequest;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment findByUrl(String url);

    String submitCardData(CardDataDto cardDataDto, String url);
}
