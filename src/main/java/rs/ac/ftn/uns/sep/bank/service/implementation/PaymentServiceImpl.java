package rs.ac.ftn.uns.sep.bank.service.implementation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.ftn.uns.sep.bank.model.Account;
import rs.ac.ftn.uns.sep.bank.model.Card;
import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.model.Transaction;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.AccountService;
import rs.ac.ftn.uns.sep.bank.service.CardService;
import rs.ac.ftn.uns.sep.bank.service.PaymentService;
import rs.ac.ftn.uns.sep.bank.service.TransactionService;
import rs.ac.ftn.uns.sep.bank.utils.Mapper;
import rs.ac.ftn.uns.sep.bank.utils.dto.AcquirerRequest;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.IssuerRequest;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${spring.application.name}")
    private String appName;

    private final PaymentRepository paymentRepository;

    private final CardService cardService;

    private final AccountService accountService;

    private final TransactionService transactionService;

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
        Account merchant = payment.getMerchant();

        if (isNull(card)) {
            LOGGER.error("Could not find any card with provided PAN number: " + cardDataDto.getPan());

            AcquirerRequest acquirerRequest = new AcquirerRequest();
            acquirerRequest.setAcquirerName(this.appName);
            acquirerRequest.setAcquirerOrderId(payment.getId().toString());
            acquirerRequest.setAcquirerTimestamp(String.valueOf(System.currentTimeMillis()));
            acquirerRequest.setHolderName(cardDataDto.getHolderName());
            acquirerRequest.setPan(cardDataDto.getPan());
            acquirerRequest.setSecurityCode(cardDataDto.getSecurityCode());
            acquirerRequest.setValidTo(cardDataDto.getValidTo());
            acquirerRequest.setAmount(payment.getAmount());

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<IssuerRequest> issuerRequest = restTemplate.postForEntity("http://localhost:8081/bankLookup", acquirerRequest, IssuerRequest.class);
            if (issuerRequest.getBody().getSuccessful()) {
                LOGGER.info(String.format("Adding payment amount to recipient: %s %s (amount: %.2f)", merchant.getClient().getFirstName(), merchant.getClient().getLastName(), merchant.getAmount()));
                merchant.setAmount(merchant.getAmount().add(payment.getAmount()));
                accountService.save(merchant);

                transaction.setValid(true);
                redirectUrl = payment.getSuccessUrl();
            } else {
                transaction.setValid(false);
                redirectUrl = payment.getFailedUrl();
            }
            transactionService.save(transaction);

            return redirectUrl;
        }

        transaction.setRecipient(payment.getMerchant());
        LOGGER.info("transaction field 'recipient' set to: " + payment.getMerchant().getId());

        transaction.setPayer(card.getAccount());
        LOGGER.info("transaction field 'payer' set to: " + card.getAccount().getId());

        transaction.setAmount(payment.getAmount());
        LOGGER.info("transaction field 'amount' set to: " + payment.getAmount());

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
    public IssuerRequest handleAcquirerRequest(AcquirerRequest acquirerRequest) {
        IssuerRequest response = Mapper.map(acquirerRequest, IssuerRequest.class);

        Transaction transaction = new Transaction();

        Card card = cardService.findByPan(acquirerRequest.getPan());

        Account buyer = card.getAccount();

        transaction.setPayer(buyer);
        LOGGER.info("transaction field 'payer' set to: " + buyer);

        final BigDecimal amount = acquirerRequest.getAmount();
        transaction.setAmount(amount);
        LOGGER.info("transaction field 'amount' set to: " + amount);

        if (buyer.getAmount().compareTo(acquirerRequest.getAmount()) < 0) {
            LOGGER.error("Buyer does not have enough money.");
            transaction.setValid(false);
            response.setSuccessful(false);
        } else {
            LOGGER.info(String.format("Subtracting payment amount from buyer: %s %s (amount: %.2f)", buyer.getClient().getFirstName(), buyer.getClient().getLastName(), buyer.getAmount()));
            buyer.setAmount(buyer.getAmount().subtract(acquirerRequest.getAmount()));

            accountService.save(buyer);

            response.setSuccessful(true);
            transaction.setValid(true);
        }

        transactionService.save(transaction);
        response.setIssuerOrderId(String.valueOf(transaction.getId()));
        response.setIssuerTimestamp(String.valueOf(System.currentTimeMillis()));

        return response;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findByUrl(String url) {
        return paymentRepository.findByUrl(url);
    }

}
