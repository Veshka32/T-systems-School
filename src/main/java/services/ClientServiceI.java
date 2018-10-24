package services;

import entities.Client;

public interface ClientServiceI {

    Client findByPhone(int phone);

    Client get(int id);

    int create(Client client);
}
