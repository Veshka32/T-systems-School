package services.interfaces;

import entities.MyUser;
import services.ServiceException;

public interface MyUserServiceI {
    void create(MyUser dto) throws ServiceException;
}
