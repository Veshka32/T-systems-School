//package services.implementations;
//
//import dao.interfaces.ClientDaoI;
//import model.dto.ClientDTO;
//import model.entity.Client;
//import model.helpers.PaginateHelper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import services.exceptions.ServiceException;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.WARN)
//class ClientServiceTest {
//    private String passport = "123457890";
//    private String email = "test@mail.com";
//    private ClientDTO dto = new ClientDTO();
//
//    @InjectMocks
//    private ClientService clientService;
//
//    @Mock
//    private ClientDaoI clientDaoI;
//
//    @BeforeEach
//    void setUp() {
//        //enable mocks
//        MockitoAnnotations.initMocks(this);
//
//        // prepare dto
//        dto.setPassportId(passport);
//        dto.setEmail(email);
//    }
//
//    @Test
//    void findByPassport() {
//        String passport = "123457890";
//        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
//        assertNull(clientService.getByPassport(passport));
//    }
//
//    @Test
//    void createTest() {
//
//        when(clientDaoI.isPassportExist(passport)).thenReturn(true);
//        ServiceException e = assertThrows(ServiceException.class, () -> clientService.create(dto));
//        assertEquals(e.getMessage(), "passportId is reserved");
//
//        when(clientDaoI.isPassportExist(passport)).thenReturn(false);
//        when(clientDaoI.isEmailExists(email)).thenReturn(true);
//        e = assertThrows(ServiceException.class, () -> clientService.create(dto));
//        assertEquals(e.getMessage(), "email is reserved");
//    }
//
//    @Test
//    void updateTest() {
//        dto.setId(2);
//        Client client = new Client();
//        client.setId(1);
//
//        //client with such a passport already exists
//        when(clientDaoI.findByPassportId(passport)).thenReturn(client);
//        ServiceException e = assertThrows(ServiceException.class, () -> clientService.update(dto));
//        assertEquals(e.getMessage(), "passportId is reserved");
//
//        //passport is unique,but not email
//        when(clientDaoI.findByPassportId(passport)).thenReturn(null);
//        when(clientDaoI.findByEmail(email)).thenReturn(client);
//        e = assertThrows(ServiceException.class, () -> clientService.update(dto));
//        assertEquals(e.getMessage(), "email is reserved");
//    }
//
//    @Test
//    void getPaginateData() {
//        long total = 10L;
//        int perPage = 3;
//        int limit = 10;
//        List<Client> clients = Collections.singletonList(new Client());
//        assertThrows(IllegalArgumentException.class, () -> clientService.getPaginateData(0, 0));
//        assertThrows(IllegalArgumentException.class, () -> clientService.getPaginateData(1, -1));
//        when(clientDaoI.allInRange(0, perPage)).thenReturn(clients);
//        when(clientDaoI.count()).thenReturn(total);
//        PaginateHelper<Client> helper = clientService.getPaginateData(null, 3);
//        assert (helper.getItems().equals(clients));
//        assert (helper.getTotal() == limit / perPage + limit % perPage); // 10clients / 3 per page=3+1 page
//    }
//}