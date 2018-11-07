package services.interfaces;

import entities.dto.MyUserDTO;
import services.ServiceException;

public interface MyUserServiceI {
    void create(MyUserDTO dto) throws ServiceException;
}
