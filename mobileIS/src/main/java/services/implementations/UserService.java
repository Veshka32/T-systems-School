package services.implementations;

import dao.interfaces.ContractDaoI;
import dao.interfaces.UserDaoI;
import model.dto.AccountDTO;
import model.entity.Contract;
import model.entity.User;
import model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.aspects.Loggable;
import services.interfaces.UserServiceI;

import java.util.Optional;

@Service
@EnableTransactionManagement
@Transactional
public class UserService implements UserServiceI {

    @Autowired
    UserDaoI userDAO;

    @Autowired
    ContractDaoI contractDaoI;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Loggable
    public Optional<String> createAccount(AccountDTO dto) {
        //check if already exists
        User user = userDAO.findByLogin(dto.getLogin());
        if (user != null) return Optional.of("This phone is already registered");

        //check if number exists
        Contract contract = contractDaoI.findByPhone(Long.parseLong(dto.getLogin()));
        if (contract == null) return Optional.of("There is no such a number");

        user = new User();
        user.setRole(Role.ROLE_CLIENT);
        user.setContract(contract);
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDAO.save(user);

        dto.setPassword(null);
        dto.setId(user.getId());
        return Optional.empty();
    }
}
