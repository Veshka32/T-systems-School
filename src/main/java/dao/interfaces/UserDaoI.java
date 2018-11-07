package dao.interfaces;

import entities.MyUser;

public interface UserDaoI extends IGenericDAO<MyUser> {
    MyUser findByLogin(String login);
}
