package services;

import entities.Client;
import entities.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.GenericDAO;
import repositories.IGenericDAO;

@Service
public class ClientService implements ClientServiceI {
    IGenericDAO<Client>  clientDAO;

    @Autowired
    public void setOptionDAO(GenericDAO<Client> optionDAO ) {
        this.clientDAO=optionDAO;
        optionDAO.setClass(Client.class);
    }

    @Override
    @Transactional
    public Client findByPhone(int phone) {
        /**
         * TODO
         */
        return null;
    }

    @Override
    public Client get(int id) {
        return clientDAO.findOne(id);
    }

    @Override
    @Transactional
    public Client create(Client client){
        int id=clientDAO.create(client);
        return clientDAO.findOne(id);
    }
}
