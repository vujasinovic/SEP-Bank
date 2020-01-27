package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.*;
import rs.ac.ftn.uns.sep.bank.properties.BankProperties;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.*;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentResponse;
import rs.ac.uns.ftn.sep.commons.dto.ExternalBankPaymentRequest;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_URL_F = "%s/card/%s";

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private static final String NOT_FOUND = "notFound";

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankProperties properties;

    @Override
    public ExternalBankPaymentResponse handleKpRequest(ExternalBankPaymentRequest kpRequestDto) {
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

    @Override
    public String submitCardData(CardDataDto cardDataDto, String url) {
        Transaction transaction = new Transaction();

        String redirectUrl;

        Payment payment = paymentRepository.findByUrl(url);

        if (!nonNull(payment)) {
            LOGGER.error("Could not find payment on provided url: " + url);
            transaction.setValid(false);
            transactionService.save(transaction);
        }

        Card card = cardService.findByPan(cardDataDto.getPan());

        if (!nonNull(card)) {
            LOGGER.error("Could not find any card with provided PAN number: " + cardDataDto.getPan());

            transaction.setValid(false);
            transactionService.save(transaction);
        }

        transaction.setRecipient(payment.getMerchant());
        LOGGER.info("transaction field 'recipient' set to: " + payment.getMerchant().getId());

        transaction.setPayer(card.getAccount());
        LOGGER.info("transaction field 'payer' set to: " + card.getAccount().getId());

        transaction.setAmount(payment.getAmount());
        LOGGER.info("transaction field 'amount' set to: " + payment.getAmount());

        Account merchant = payment.getMerchant();
        Account account = card.getAccount();

        if (account.getAmount().compareTo(payment.getAmount()) < 0) {
            LOGGER.error("Payer does not have enough money.");
            transaction.setValid(false);
            redirectUrl = payment.getFailedUrl();
        } else {
            redirectUrl = payment.getSuccessUrl();

            LOGGER.info(String.format("Subtracting payment amount from payer: %s %s (amount: %.2f)", account.getClient().getFirstName(), account.getClient().getLastName(), account.getAmount()));
            account.setAmount(account.getAmount().subtract(payment.getAmount()));

            LOGGER.info(String.format("Adding payment amount to recipient: %s %s (amount: %.2f)", merchant.getClient().getFirstName(), merchant.getClient().getLastName(), merchant.getAmount()));
            merchant.setAmount(merchant.getAmount().add(payment.getAmount()));

            accountService.save(account);
            accountService.save(merchant);

            transaction.setValid(true);
        }

        transactionService.save(transaction);

        return redirectUrl;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findByUrl(String url) {
        return paymentRepository.findByUrl(url);
    }

    private String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(10, 99));
    }

    private String generateRedirectUrl(String code) {
        return String.format(PAYMENT_URL_F, properties.getUrl(), code);
    }
}
