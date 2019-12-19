package rs.ac.ftn.uns.sep.bank.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.ac.ftn.uns.sep.bank.model.Client;
import rs.ac.ftn.uns.sep.bank.model.Payment;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.ClientService;
import rs.ac.ftn.uns.sep.bank.service.PaymentService;
import rs.ac.ftn.uns.sep.bank.utils.dto.KpRequestDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.PaymentDto;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final ClientService clientService;

    public PaymentServiceImpl(ClientServiceImpl clientService, PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.clientService = clientService;
    }

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

    private String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(10, 99));
    }
}
