package dao.interfaces;

import model.entity.Client;

public interface ClientDaoI extends IGenericDAO<Client> {

    Client findByPassportId(String passport);

    Client findByEmail(String email);
}
