package services;

import dao.interfaces.ContractDaoI;
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
import services.implementations.UserService;

import java.util.Optional;

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
    private ContractDaoI contractDao;


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
        Optional<String> e = userService.createClientAccount(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "This phone is already registered");

        //check if number exists
        when(userDAO.findByLogin(login)).thenReturn(null);
        when(contractDao.findByPhone(Long.parseLong(login))).thenReturn(null);
        e = userService.createClientAccount(dto);
        assertTrue(e.isPresent());
        assertEquals(e.get(), "There is no such a number");

        when(userDAO.findByLogin(login)).thenReturn(null);
        when(contractDao.findByPhone(Long.parseLong(login))).thenReturn(new Contract());
        assertFalse(userService.createClientAccount(dto).isPresent());
    }
}