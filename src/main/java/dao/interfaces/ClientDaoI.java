package dao.interfaces;

import entities.Client;

public interface ClientDaoI extends IGenericDAO<Client> {
    boolean isPassportExist(String id);

    boolean isEmailExists(String email);

    Client findByPassportId(String passport);

    Client findByEmail(String email);
}
