package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.*;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.*;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.KpRequestDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.PaymentDto;

import java.util.List;
import java.util.Objects;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

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

    @Override
    public PaymentDto handleKpRequest(KpRequestDto kpRequestDto) {
        LOGGER.info("Processing KP request: " + kpRequestDto);
        Payment payment = new Payment();

        PaymentDto response = new PaymentDto();

        Client client = clientService.findByMerchantId(kpRequestDto.getMerchantId());

        Payment savedPayment = new Payment();

        if (client == null) {
            LOGGER.error("Client with provided merchant id was not found.");
        } else {
            LOGGER.info(String.format("Found client with id: %s", client.getMerchantId()));

            payment.setUrl(generateTimeStamp());
            payment.setAmount(kpRequestDto.getAmount());
            payment.setMerchant(client.getAccount());

            payment.setSuccessUrl(kpRequestDto.getSuccessUrl());
            payment.setFailedUrl(kpRequestDto.getFailedUrl());
            payment.setErrorUrl(kpRequestDto.getErrorUrl());

            LOGGER.info("Persisting payment.");

            savedPayment = paymentRepository.save(payment);
            LOGGER.info("Payment persisted successfully. Payment: " + savedPayment);
        }

        LOGGER.info("Generating response with payment url " + savedPayment.getUrl());
        response.setUrl(savedPayment.getUrl());
        response.setId(savedPayment.getId());

        return response;
    }

    @Override
    public String submitCardData(CardDataDto cardDataDto, String url) {
        LOGGER.info("Card data: " + cardDataDto.toString());

        Transaction transaction = new Transaction();
        LOGGER.info("Created Transaction object");

        String redirectUrl;

        Payment payment = paymentRepository.findByUrl(url);

        if (payment == null) {
            LOGGER.error("Could not find payment on provided url: " + url);
            transaction.setValid(false);
            transactionService.save(transaction);

            return payment.getErrorUrl();
        }

        Card card = cardService.findByPan(cardDataDto.getPan());

        if (card == null) {
            LOGGER.error("Could not find any card with provided PAN number: " + cardDataDto.getPan());
            LOGGER.info("Redirecting to error URL: " + payment.getErrorUrl());

            transaction.setValid(false);
            transactionService.save(transaction);

            return payment.getErrorUrl();
        }

        LOGGER.info(String.format("Recipient: %s %s", payment.getMerchant().getClient().getFirstName(), payment.getMerchant().getClient().getLastName()));
        transaction.setRecipient(payment.getMerchant());
        LOGGER.info("transaction field 'recipient' set to: " + payment.getMerchant().getId());

        LOGGER.info(String.format("Payer: %s %s", card.getAccount().getClient().getFirstName(), card.getAccount().getClient().getLastName()));
        transaction.setPayer(card.getAccount());
        LOGGER.info("transaction field 'payer' set to: " + card.getAccount().getId());

        transaction.setAmount(payment.getAmount());
        LOGGER.info("transaction field 'amount' set to: " + payment.getAmount());

        LOGGER.info("Found payment: " + payment.toString());



        Account merchant = payment.getMerchant();

        Account account = card.getAccount();

        if (account.getAmount().compareTo(payment.getAmount()) < 0) {
            LOGGER.error("Payer does not have enough money.");
            transaction.setValid(false);
            redirectUrl = payment.getFailedUrl();
        } else {
            LOGGER.info("Payer has enough money.");
            redirectUrl = payment.getSuccessUrl();

            LOGGER.info(String.format("Subtracting payment amount from payer: %s %s", account.getClient().getFirstName(), account.getClient().getLastName()));
            account.setAmount(account.getAmount().subtract(payment.getAmount()));
            LOGGER.info("Payer account amount after subtraction: " + account.getAmount());

            LOGGER.info(String.format("Adding payment amount to recipient: %s %s", merchant.getClient().getFirstName(), merchant.getClient().getLastName()));
            merchant.setAmount(merchant.getAmount().add(payment.getAmount()));
            LOGGER.info("Recipient account amount after addition: " + merchant.getAmount());

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
}
