package services.implementations;

import dao.interfaces.UserDaoI;
import model.dto.MyUserDTO;
import model.entity.Contract;
import model.entity.MyUser;
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
import services.ServiceException;
import services.interfaces.ContractServiceI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserDaoI userDAO;

    @Mock
    ContractServiceI contractService;

    @Mock
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void create() {
        MyUserDTO dto = new MyUserDTO();
        String login = "1234567890";
        dto.setLogin(login);
        dto.setPassword("password");

        //check if login is reserved
        when(userDAO.findByLogin(login)).thenReturn(new MyUser());
        ServiceException e = assertThrows(ServiceException.class, () -> userService.createClient(dto));
        assertEquals(e.getMessage(), "This phone is already registered");

        //check if number exists
        when(userDAO.findByLogin(login)).thenReturn(null);
        when(contractService.findByPhone(Long.parseLong(login))).thenReturn(null);
        e = assertThrows(ServiceException.class, () -> userService.createClient(dto));
        assertEquals(e.getMessage(), "There is no such a number");

        //check fields update
        when(contractService.findByPhone(Long.parseLong(login))).thenReturn(1);
        when(contractService.get(1)).thenReturn(new Contract());
        when(userDAO.save(new MyUser())).thenReturn(1);
        assertDoesNotThrow(() -> userService.createClient(dto));
    }
}