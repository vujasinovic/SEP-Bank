package rs.ac.ftn.uns.sep.bank.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.ftn.uns.sep.bank.service.ClientService;
import rs.ac.ftn.uns.sep.bank.service.implementation.ClientServiceImpl;
import rs.ac.ftn.uns.sep.bank.utils.dto.KpRequestDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.PaymentDto;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final ClientService clientService;

    public PaymentController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public PaymentDto postPayment(KpRequestDto kpRequestDto) {
        return null;
    }
}
