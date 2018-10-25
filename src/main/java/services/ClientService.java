package services;

import entities.Client;
import entities.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.ContractDAO;
import repositories.GenericDAO;
import repositories.IGenericDAO;

import java.util.List;

@Service
@Transactional
public class ClientService {
    IGenericDAO<Client>  clientDAO;
    ContractDAO contractDAO;

    @Autowired
    public void setOptionDAO(GenericDAO<Client> optionDAO ) {
        this.clientDAO=optionDAO;
        optionDAO.setClass(Client.class);
    }

    public Client get(int id) {
        return clientDAO.findOne(id);
    }

    public int create(Client client){
        return clientDAO.create(client);
    }

    public List<Client> getAll() {
        return clientDAO.findAll();
    }

    public void addContract(int clientId, Contract contract) {
        Client client=clientDAO.findOne(clientId);
        contract.setOwner(client);
        client.addContract(contract);
        clientDAO.create(client);
    }
}
