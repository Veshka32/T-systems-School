package services.interfaces;

import entities.Client;
import entities.dto.ClientDTO;
import services.ServiceException;

import java.util.List;

/**
 * The {@code ClientServiceI} interface is a service-layer class for manipulating with {@code Client} entity.
 * <p>
 *
 * @author Natalia Makarchuk
 */

public interface ClientServiceI {
    /**
     * Return {@code Client} entity from database with given id.
     *
     * @param id database id of desired entity
     * @return {@code Client} found by given id or {@code null} if there is no entity with given id
     */
    Client get(int id);

    Client findByPassport(String passport);

    ClientDTO getDTO(int id);

    void create(ClientDTO dto) throws ServiceException;

    void update(ClientDTO dto) throws ServiceException;

    void delete(int id);

    List<Client> getAll();
}
