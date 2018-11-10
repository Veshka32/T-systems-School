package services.interfaces;

import model.dto.ClientDTO;
import model.entity.Client;
import services.ServiceException;

/**
 * The {@code ClientServiceI} interface is a service-layer class for manipulating with {@code Client} entity.
 * <p>
 *
 * @author Natalia Makarchuk
 */

public interface ClientServiceI extends GenericServiceI<Client> {

    Client findByPassport(String passport);

    ClientDTO getDTO(int id);

    void create(ClientDTO dto) throws ServiceException;

    void update(ClientDTO dto) throws ServiceException;

    void delete(int id);
}
