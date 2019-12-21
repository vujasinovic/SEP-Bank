package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Account;
import rs.ac.ftn.uns.sep.bank.model.Card;
import rs.ac.ftn.uns.sep.bank.model.Client;
import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.AccountService;
import rs.ac.ftn.uns.sep.bank.service.CardService;
import rs.ac.ftn.uns.sep.bank.service.ClientService;
import rs.ac.ftn.uns.sep.bank.service.PaymentService;
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

    @Override
    public PaymentDto handleKpRequest(KpRequestDto kpRequestDto) {
        LOGGER.debug("Processing KP request");
        Payment payment = new Payment();

        PaymentDto response = new PaymentDto();

        Client client = clientService.findByMerchantId(kpRequestDto.getMerchantId());

        Payment savedPayment = new Payment();

        if (client != null) {
            LOGGER.debug(String.format("Found client with id: %s", client.getMerchantId()));

            payment.setUrl(generateTimeStamp());
            payment.setAmount(kpRequestDto.getAmount());
            payment.setAccount(client.getAccount());

            LOGGER.debug("Persisting payment.");

            savedPayment = paymentRepository.save(payment);
            LOGGER.debug("Payment persisted successfully");
        }

        LOGGER.debug("Generating response..");
        response.setUrl(savedPayment.getUrl());
        response.setId(savedPayment.getId());

        return response;
    }

    @Override
    public String submitCardData(CardDataDto cardDataDto, String url) {
        String redirectUrl;

        Payment payment = paymentRepository.findByUrl(url);

        Card card = cardService.findByPan(cardDataDto.getPan());

        if (card == null) {
            return payment.getErrorUrl();
        }

        Account merchant = payment.getMerchant();

        Account account = card.getAccount();

        if (account.getAmount().compareTo(payment.getAmount()) < 0) {
            redirectUrl = payment.getFailedUrl();
        } else {
            redirectUrl = payment.getSuccessUrl();

            account.setAmount(account.getAmount().subtract(payment.getAmount()));

            merchant.setAmount(merchant.getAmount().add(payment.getAmount()));

            accountService.save(account);
            accountService.save(merchant);
        }

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
