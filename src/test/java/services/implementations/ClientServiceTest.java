package services.implementations;

import dao.interfaces.ClientDaoI;
import entities.Client;
import entities.dto.ClientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import services.ServiceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ClientServiceTest {
    String passport = "123457890";
    String email = "test@mail.com";
    ClientDTO dto = new ClientDTO();

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientDaoI clientDaoI;

    @BeforeEach
    public void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);

        // prepare dto
        dto.setPassportId(passport);
        dto.setEmail(email);
    }

    @Test
    void findByPassport() {
        String passport = "123457890";
        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
        assertNull(clientService.findByPassport(passport));
    }

    @Test
    void createTest() {

        when(clientDaoI.isPassportExist(passport)).thenReturn(true);
        ServiceException e = assertThrows(ServiceException.class, () -> clientService.create(dto));
        assertEquals(e.getMessage(), "passportId is reserved");

        when(clientDaoI.isPassportExist(passport)).thenReturn(false);
        when(clientDaoI.isEmailExists(email)).thenReturn(true);
        e = assertThrows(ServiceException.class, () -> clientService.create(dto));
        assertEquals(e.getMessage(), "email is reserved");
    }

    @Test
    void updateTest() {
        dto.setId(2);
        Client client = new Client();
        client.setId(1);

        //client with such a passport already exists
        when(clientDaoI.findByPassportId(passport)).thenReturn(client);
        ServiceException e = assertThrows(ServiceException.class, () -> clientService.update(dto));
        assertEquals(e.getMessage(), "passportId is reserved");

        //passport is unique,but not email
        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
        when(clientDaoI.findByEmail(email)).thenReturn(client);
        e = assertThrows(ServiceException.class, () -> clientService.update(dto));
        assertEquals(e.getMessage(), "email is reserved");
    }
}