package services.implementations;

import dao.interfaces.UserDaoI;
import model.dto.AccountDTO;
import model.entity.Account;
import model.entity.Contract;
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
class AccountServiceTest {
    @Mock
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    @InjectMocks
    private AccountService accountService;
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
        when(userDAO.findByLogin(login)).thenReturn(new Account());
        AccountCreateException e = assertThrows(AccountCreateException.class, () -> accountService.createAccount(dto));
        assertEquals(e.getMessage(), "This phone is already registered");

        //check if number exists
        when(userDAO.findByLogin(login)).thenReturn(null);
        when(contractService.findByPhone(Long.parseLong(login))).thenReturn(null);
        e = assertThrows(AccountCreateException.class, () -> accountService.createAccount(dto));
        assertEquals(e.getMessage(), "There is no such a number");

        //check fields update
        when(contractService.findByPhone(Long.parseLong(login))).thenReturn(1);
        when(contractService.get(1)).thenReturn(new Contract());
        when(userDAO.save(new Account())).thenReturn(1);
        assertDoesNotThrow(() -> accountService.createAccount(dto));
    }
}