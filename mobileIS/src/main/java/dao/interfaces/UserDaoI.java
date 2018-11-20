package dao.interfaces;

import model.entity.User;

public interface UserDaoI extends IGenericDAO<User> {
    User findByLogin(String login);
}
