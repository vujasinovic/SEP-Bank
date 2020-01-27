package rs.ac.ftn.uns.sep.bank.service.implementation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Account;
import rs.ac.ftn.uns.sep.bank.model.Card;
import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.model.Transaction;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.AccountService;
import rs.ac.ftn.uns.sep.bank.service.CardService;
import rs.ac.ftn.uns.sep.bank.service.PaymentService;
import rs.ac.ftn.uns.sep.bank.service.TransactionService;
import rs.ac.ftn.uns.sep.bank.utils.dto.CardDataDto;

import java.util.List;

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

}
