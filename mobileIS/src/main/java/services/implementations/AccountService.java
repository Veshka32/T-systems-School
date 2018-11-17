package services.implementations;

import dao.interfaces.UserDaoI;
import model.dto.AccountDTO;
import model.entity.Account;
import model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.exceptions.AccountCreateException;
import services.interfaces.AccountServiceI;
import services.interfaces.ContractServiceI;

@Service
@EnableTransactionManagement
@Transactional
public class AccountService implements AccountServiceI {

    @Autowired
    UserDaoI userDAO;

    @Autowired
    ContractServiceI contractService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public int createAccount(AccountDTO dto) throws AccountCreateException {
        //check if already exists
        Account user = userDAO.findByLogin(dto.getLogin());
        if (user != null) throw new AccountCreateException("This phone is already registered");

        //check if number exists
        Integer id = contractService.findByPhone(Long.parseLong(dto.getLogin()));
        if (id == null) throw new AccountCreateException("There is no such a number");

        user = new Account();
        user.setRole(Role.ROLE_CLIENT);
        user.setContract(contractService.get(id));
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDAO.save(user);

        dto.setPassword(null);
        return user.getId();
    }
}
