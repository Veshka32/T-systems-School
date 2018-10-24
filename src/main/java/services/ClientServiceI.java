package services;

import entities.Client;
import entities.Contract;

public interface ClientServiceI {

    Client findByPhone(int phone);

    Client get(int id);

    int create(Client client);
    
    void addContract(int clientId, Contract contract);
}
