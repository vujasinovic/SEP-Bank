package rs.ac.ftn.uns.sep.bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.service.AccountService;
import rs.ac.ftn.uns.sep.bank.service.PaymentService;
import rs.ac.ftn.uns.sep.bank.service.implementation.AccountServiceImpl;
import rs.ac.ftn.uns.sep.bank.service.implementation.PaymentServiceImpl;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.KpRequestDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.PaymentDto;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin("http://localhost:3000")
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    private final AccountService accountService;

    public PaymentController(PaymentServiceImpl paymentService, AccountServiceImpl accountService) {
        this.paymentService = paymentService;
        this.accountService = accountService;
    }

    @PostMapping
    public PaymentDto postPaymentRequest(KpRequestDto kpRequestDto) {
        logger.debug("Requesting payment");
        logger.debug(String.format("Request: \n %s", kpRequestDto.toString()));

        return paymentService.handleKpRequest(kpRequestDto);
    }

    @GetMapping("/{url}")
    public Payment getPaymentPage(@PathVariable String url) {
        return paymentService.findByUrl(url);
    }

    @PostMapping("/{url}")
    public Map<String, String> postCardData(CardDataDto cardDataDto, @PathVariable String url) {
        return Map.of("url", paymentService.submitCardData(cardDataDto, url));
    }

}
