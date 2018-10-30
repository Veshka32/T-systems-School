package repositories.interfaces;

import entities.MyUser;
import repositories.interfaces.IGenericDAO;

public interface UserDaoI extends IGenericDAO<MyUser> {
    MyUser findByLogin(String login);
}
