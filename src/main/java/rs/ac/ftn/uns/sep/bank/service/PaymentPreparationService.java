package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentRequest;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentResponse;

public interface PaymentPreparationService {
    ExternalBankPaymentResponse preparePayment(ExternalBankPaymentRequest kpRequestDto);
}
