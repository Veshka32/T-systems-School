package services.implementations;

import dao.interfaces.UserDaoI;
import model.dto.AccountDTO;
import model.entity.Contract;
import model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import services.exceptions.AccountCreateException;
import services.interfaces.ContractServiceI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class UserServiceTest {
    @Mock
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    @InjectMocks
    private UserService userService;
    @Mock
    private UserDaoI userDAO;
    @Mock
    private ContractServiceI contractService;


    @BeforeEach
    void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create() {
        AccountDTO dto = new AccountDTO();
        String login = "1234567890";
        dto.setLogin(login);
        dto.setPassword("password");

        //check if login is reserved
        when(userDAO.findByLogin(login)).thenReturn(new User());
        AccountCreateException e = assertThrows(AccountCreateException.class, () -> userService.createAccount(dto));
        assertEquals(e.getMessage(), "This phone is already registered");

        //check if number exists
        when(userDAO.findByLogin(login)).thenReturn(null);
        when(contractService.getJson(login)).thenReturn(null);
        e = assertThrows(AccountCreateException.class, () -> userService.createAccount(dto));
        assertEquals(e.getMessage(), "There is no such a number");

        //check fields update
        when(contractService.getJson(login)).thenReturn("");
        when(contractService.get(1)).thenReturn(new Contract());
        when(userDAO.save(new User())).thenReturn(1);
        assertDoesNotThrow(() -> userService.createAccount(dto));
    }
}