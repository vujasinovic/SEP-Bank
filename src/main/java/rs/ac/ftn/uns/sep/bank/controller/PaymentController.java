package rs.ac.ftn.uns.sep.bank.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.service.AccountService;
import rs.ac.ftn.uns.sep.bank.service.PaymentPreparationService;
import rs.ac.ftn.uns.sep.bank.service.PaymentService;
import rs.ac.ftn.uns.sep.bank.service.implementation.AccountServiceImpl;
import rs.ac.ftn.uns.sep.bank.service.implementation.PaymentServiceImpl;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentResponse;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentRequest;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    private final PaymentPreparationService paymentPreparationService;

    @PostMapping
    public ExternalBankPaymentResponse postPaymentRequest(@RequestBody ExternalBankPaymentRequest kpRequestDto) {
        logger.debug("Requesting payment");
        logger.debug(String.format("Request: \n %s", kpRequestDto.toString()));

        return paymentPreparationService.preparePayment(kpRequestDto);
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
