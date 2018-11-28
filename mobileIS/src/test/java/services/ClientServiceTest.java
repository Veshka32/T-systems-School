package services;

import dao.interfaces.ClientDaoI;
import dao.interfaces.ContractDaoI;
import model.dto.ClientDTO;
import model.entity.Client;
import model.helpers.PaginateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import services.implementations.ClientService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ClientServiceTest {
    private String passport = "1234567890";
    private String email = "test@mail.com";
    private ClientDTO dto = new ClientDTO();

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientDaoI clientDaoI;

    @Mock
    private ContractDaoI contractDaoI;

    @BeforeEach
    void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);

        // prepare dto
        dto.setPassportId(passport);
        dto.setEmail(email);
    }

    @Test
    void getByPassportTest() {
        assertThrows(NumberFormatException.class, () -> clientService.getByPassport("123457890"));
        assertThrows(NumberFormatException.class, () -> clientService.getByPassport("123456789a"));
        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
        assertNull(clientService.getByPassport(passport));
        when(clientDaoI.findByPassportId(passport)).thenReturn(new Client());
        assertNotNull(clientService.getByPassport(passport));
    }

    @Test
    void getJsonByPassportTest() {
        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
        String result = clientService.getJsonByPassport(passport);
        assertEquals(result, "{\"status\":\"error\",\"message\":\"there is no such client\"}");
        Client client = new Client();
        client.setName("test");
        client.setBirthday(LocalDate.of(2015, 1, 1));
        when(clientDaoI.findByPassportId(passport)).thenReturn(client);
        result = clientService.getJsonByPassport(passport);
        assertEquals(result, "{\"status\":\"success\",\"client\":{\"id\":0,\"name\":\"test\",\"birthday\":{\"year\":2015,\"month\":1,\"day\":1}}}");
    }


    @Test
    void getByPhoneTest() {
        assertThrows(NumberFormatException.class, () -> clientService.getByPhone("123457890"));
        assertThrows(NumberFormatException.class, () -> clientService.getByPhone("123456789a"));
        when(contractDaoI.findClientByPhone(Long.parseLong(passport))).thenReturn(null);
        assertNull(clientService.getByPhone(passport));
        when(contractDaoI.findClientByPhone(Long.parseLong(passport))).thenReturn(new Client());
        assertNotNull(clientService.getByPhone(passport));
    }

    @Test
    void createTest() {

        when(clientDaoI.findByPassportId(passport)).thenReturn(new Client());
        Optional<String> e = clientService.create(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "passportId is reserved");

        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
        when(clientDaoI.findByEmail(email)).thenReturn(new Client());
        e = clientService.create(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "email is reserved");

        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
        when(clientDaoI.findByEmail(email)).thenReturn(null);
        e = clientService.create(dto);
        assertFalse(e.isPresent());
    }

    @Test
    void updateTest() {
        dto.setId(2);
        Client client = new Client();
        client.setId(1);

        //client with such a passport already exists
        when(clientDaoI.findByPassportId(passport)).thenReturn(client);
        Optional e = clientService.update(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "passportId is reserved");

        //passport is unique,but not email
        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
        when(clientDaoI.findByEmail(email)).thenReturn(client);
        e = clientService.update(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "email is reserved");
    }

    @Test
    void getPaginateData() {
        long total = 10L;
        int perPage = 3;
        int limit = 10;
        List<Client> clients = Collections.singletonList(new Client());
        assertThrows(IllegalArgumentException.class, () -> clientService.getPaginateData(0, 0));
        assertThrows(IllegalArgumentException.class, () -> clientService.getPaginateData(1, -1));
        when(clientDaoI.allInRange(0, perPage)).thenReturn(clients);
        when(clientDaoI.count()).thenReturn(total);
        PaginateHelper<Client> helper = clientService.getPaginateData(null, 3);
        assert (helper.getItems().equals(clients));
        assert (helper.getTotal() == limit / perPage + limit % perPage); // 10clients / 3 per page=3+1 page
    }
}