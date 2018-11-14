package services.implementations;

import dao.interfaces.UserDaoI;
import model.dto.MyUserDTO;
import model.entity.MyUser;
import model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.ServiceException;
import services.interfaces.ContractServiceI;
import services.interfaces.UserServiceI;

@Service
@EnableTransactionManagement
@Transactional
public class UserService implements UserServiceI {

    @Autowired
    UserDaoI userDAO;

    @Autowired
    ContractServiceI contractService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public int createClient(MyUserDTO dto) throws ServiceException {
        //check if already exists
        MyUser user=userDAO.findByLogin(dto.getLogin());
        if (user!=null) throw new ServiceException("This phone is already registered");

        //check if number exists
        Integer id = contractService.findByPhone(Long.parseLong(dto.getLogin()));
        if (id == null) throw new ServiceException("There is no such a number");

        user = new MyUser();
        user.setRole(Role.ROLE_CLIENT);
        user.setContract(contractService.get(id));
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userDAO.save(user);

        dto.setPassword(null);
        return user.getId();
    }
}
