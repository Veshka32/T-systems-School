package services.interfaces;

import entities.dto.MyUserDTO;
import services.ServiceException;

public interface MyUserServiceI {
    int create(MyUserDTO dto) throws ServiceException;
}
