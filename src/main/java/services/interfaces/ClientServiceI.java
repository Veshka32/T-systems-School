package services.interfaces;

import entities.Client;
import entities.dto.ClientDTO;
import services.ServiceException;

import java.util.List;

public interface ClientServiceI {

    Client get(int id);

    Client findByPassport(String passport);

    ClientDTO getDTO(int id);

    void create(ClientDTO dto) throws ServiceException;

    void update(ClientDTO dto) throws ServiceException;

    void delete(int id);

    List<Client> getAll();
}
