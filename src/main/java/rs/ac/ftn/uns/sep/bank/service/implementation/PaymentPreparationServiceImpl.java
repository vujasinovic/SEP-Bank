package rs.ac.ftn.uns.sep.bank.service.implementation;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Client;
import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.properties.BankProperties;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.ClientService;
import rs.ac.ftn.uns.sep.bank.service.PaymentPreparationService;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentRequest;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentResponse;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PaymentPreparationServiceImpl implements PaymentPreparationService {
    private final static Logger LOGGER = LogManager.getLogger(PaymentPreparationServiceImpl.class);

    private static final String PAYMENT_URL_F = "%s/card/%s";
    private static final String NOT_FOUND = "notFound";

    private final ClientService clientService;

    private final PaymentRepository paymentRepository;

    private final BankProperties properties;

    @Override
    public ExternalBankPaymentResponse preparePayment(ExternalBankPaymentRequest kpRequestDto) {
        LOGGER.info("Processing KP request: " + kpRequestDto);
        Payment payment = new Payment();

        ExternalBankPaymentResponse response = new ExternalBankPaymentResponse();

        LOGGER.info("Finding client by provided merchant id: " + kpRequestDto.getMerchantId());
        Client client = clientService.findByMerchantId(kpRequestDto.getMerchantId());

        Payment savedPayment = new Payment();

        if (!nonNull(client)) {
            LOGGER.error("Client with provided merchant id was not found.");
            response.setUrl(NOT_FOUND);
        } else {
            payment.setUrl(generateTimeStamp());
            LOGGER.info("Generated payment URL: " + payment.getUrl());

            payment.setAmount(kpRequestDto.getAmount());
            payment.setMerchant(client.getAccount());

            payment.setSuccessUrl(kpRequestDto.getSuccessUrl());
            payment.setFailedUrl(kpRequestDto.getFailedUrl());
            payment.setErrorUrl(kpRequestDto.getErrorUrl());

            LOGGER.info(String.format("Success url: %s, Failed url: %s, Error url: %s", payment.getSuccessUrl(), payment.getFailedUrl(), payment.getErrorUrl()));

            savedPayment = paymentRepository.save(payment);
            LOGGER.info("Payment persisted successfully. Payment: " + savedPayment);
        }

        LOGGER.info("Generating response with payment url " + savedPayment.getUrl());

        response.setUrl(generateRedirectUrl(savedPayment.getUrl()));
        response.setId(savedPayment.getId());

        LOGGER.info("Generated response: " + response.toString());

        return response;
    }

    private String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(10, 99));
    }

    private String generateRedirectUrl(String code) {
        return String.format(PAYMENT_URL_F, properties.getUrl(), code);
    }
}
