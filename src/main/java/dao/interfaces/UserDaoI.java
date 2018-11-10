package dao.interfaces;

import model.entity.MyUser;

public interface UserDaoI extends IGenericDAO<MyUser> {
    MyUser findByLogin(String login);
}
