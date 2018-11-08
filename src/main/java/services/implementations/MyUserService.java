package services.implementations;

import dao.interfaces.UserDaoI;
import entities.Contract;
import entities.MyUser;
import entities.dto.MyUserDTO;
import entities.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.ServiceException;
import services.interfaces.ContractServiceI;
import services.interfaces.MyUserServiceI;

@Service
@EnableTransactionManagement
@Transactional
public class MyUserService implements MyUserServiceI {

    @Autowired
    UserDaoI userDAO;

    @Autowired
    ContractServiceI contractService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public int create(MyUserDTO dto) throws ServiceException {
        //check if already exists
        MyUser user=userDAO.findByLogin(dto.getLogin());
        if (user!=null) throw new ServiceException("This phone is already registered");

        //check if number exists
        Contract contract=contractService.findByPhone(Long.parseLong(dto.getLogin()));
        if (contract==null) throw new ServiceException("There is now such a number");

        user = new MyUser();
        user.setRole(Role.ROLE_CLIENT);
        user.setContract(contract);
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDAO.save(user);

        dto.setPassword(null);
        return user.getId();
    }
}
