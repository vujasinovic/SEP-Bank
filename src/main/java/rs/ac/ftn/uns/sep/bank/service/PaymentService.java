package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.KpRequestDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment findByUrl(String url);

    PaymentDto handleKpRequest(KpRequestDto kpRequestDto);

    String submitCardData(CardDataDto cardDataDto, String url);

}
