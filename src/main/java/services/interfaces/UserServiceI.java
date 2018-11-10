package services.interfaces;

import model.dto.MyUserDTO;
import services.ServiceException;

public interface UserServiceI {
    int createClient(MyUserDTO dto) throws ServiceException;
}
