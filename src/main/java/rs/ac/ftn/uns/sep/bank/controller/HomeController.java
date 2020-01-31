package rs.ac.ftn.uns.sep.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rs.ac.ftn.uns.sep.bank.service.PaymentService;
import rs.ac.ftn.uns.sep.bank.utils.dto.AcquirerRequest;
import rs.ac.ftn.uns.sep.bank.utils.dto.IssuerRequest;

@RequestMapping(value = "/")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final PaymentService paymentService;

    @GetMapping("/pcc")
    public IssuerRequest handlePccRequest(AcquirerRequest acquirerRequest) {
        IssuerRequest issuerRequest = paymentService.handleAcquirerRequest(acquirerRequest);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://localhost/pcc/handleTransactionResult", issuerRequest, IssuerRequest.class);

        return issuerRequest;
    }

}
