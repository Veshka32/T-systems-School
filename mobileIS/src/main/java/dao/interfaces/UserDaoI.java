package dao.interfaces;

import model.entity.Account;

public interface UserDaoI extends IGenericDAO<Account> {
    Account findByLogin(String login);
}
