package services;

import entities.Client;

public interface ClientServiceI {

    Client findByPhone(int phone);

    Client get(int id);

    Client create(Client client);
}
