package services.implementations;

import entities.Contract;
import entities.MyUser;
import entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.implementations.UserDAO;
import services.ServiceException;
import services.interfaces.MyUserServiceI;

@Service
@Transactional
public class MyUserService implements MyUserServiceI {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ContractService contractService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void create(MyUser dto) throws ServiceException {
        //check if already exists
        MyUser user=userDAO.findByLogin(dto.getLogin());
        if (user!=null) throw new ServiceException("This phone is already registered");

        //check if number exists
        Contract contract=contractService.findByPhone(Long.parseLong(dto.getLogin()));
        if (contract==null) throw new ServiceException("There is now such a number");

        //hash password
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        dto.setRole(Role.ROLE_CLIENT);
        dto.setContract(contract);
        userDAO.save(dto);
    }
}
