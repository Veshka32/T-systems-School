package repositories.interfaces;

import entities.Client;
import repositories.interfaces.IGenericDAO;

public interface ClientDaoI extends IGenericDAO<Client> {
    boolean isPassportExist(String id);

    boolean isEmailExists(String email);

    Client findByPassportId(String passport);

    Client findByEmail(String email);
}
