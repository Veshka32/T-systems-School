package services;

import entities.Client;
import entities.Contract;
import entities.MyUser;
import entities.Role;
import entities.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ClientDAO;
import repositories.ContractDAO;
import repositories.UserDAO;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class MyUserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ContractService contractService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void create(MyUser dto) throws ServiceException{
        //check if already exists
        MyUser user=userDAO.findByLogin(dto.getLogin());
        if (user!=null) throw new ServiceException("This phone is already registered");

        //check if number exists
        Client client=contractService.findClientByPhone(Long.parseLong(dto.getLogin()));
        if (client==null) throw new ServiceException("There is now such a number");

        //hash password
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        dto.setRole(Role.ROLE_CLIENT);
        dto.setClient(client);
        userDAO.save(dto);
    }
}
