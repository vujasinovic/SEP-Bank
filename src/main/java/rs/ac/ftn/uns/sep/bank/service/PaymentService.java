package rs.ac.ftn.uns.sep.bank.service;

import rs.ac.ftn.uns.sep.bank.utils.dto.KpRequestDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.PaymentDto;

public interface PaymentService {
    PaymentDto handleKpRequest(KpRequestDto kpRequestDto);
}
