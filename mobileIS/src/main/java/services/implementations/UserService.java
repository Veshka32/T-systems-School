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
import services.exceptions.AccountCreateException;
import services.interfaces.UserServiceI;

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
    public int createAccount(AccountDTO dto) throws AccountCreateException {
        //check if already exists
        User user = userDAO.findByLogin(dto.getLogin());
        if (user != null) throw new AccountCreateException("This phone is already registered");

        //check if number exists
        Contract contract = contractDaoI.findByPhone(Long.parseLong(dto.getLogin()));
        if (contract == null) throw new AccountCreateException("There is no such a number");

        user = new User();
        user.setRole(Role.ROLE_CLIENT);
        user.setContract(contract);
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDAO.save(user);

        dto.setPassword(null);
        return user.getId();
    }
}
