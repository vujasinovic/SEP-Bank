package rs.ac.ftn.uns.sep.bank.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.ftn.uns.sep.bank.model.Client;
import rs.ac.ftn.uns.sep.bank.repository.ClientRepository;
import rs.ac.ftn.uns.sep.bank.repository.PaymentRepository;
import rs.ac.ftn.uns.sep.bank.service.implementation.ClientServiceImpl;
import rs.ac.ftn.uns.sep.bank.service.implementation.PaymentServiceImpl;
import rs.ac.ftn.uns.sep.bank.utils.dto.KpRequestDto;
import rs.ac.ftn.uns.sep.bank.utils.dto.PaymentDto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaymentServiceTest {

    private PaymentService paymentService;

    private ClientServiceImpl clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private KpRequestDto kpRequestDto;

    @Before
    public void setUp() {
        clientService = new ClientServiceImpl(clientRepository);

        paymentService = new PaymentServiceImpl(clientService, paymentRepository);

        kpRequestDto = new KpRequestDto("merchant1",
                "password",
                BigInteger.valueOf((long) 100.0),
                1,
                LocalDateTime.now(),
                "successUrl",
                "failUrl",
                "errorUrl");
    }

    @Test
    public void testPayment() {
        List<Client> clients = clientService.findAll();
        for (Client c : clients) {
            System.out.println(c.getMerchantId());
        }

        PaymentDto response = paymentService.handleKpRequest(kpRequestDto);
        assertNotNull(response);
    }
}
