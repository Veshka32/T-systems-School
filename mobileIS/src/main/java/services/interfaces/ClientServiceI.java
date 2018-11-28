package services.interfaces;

import model.dto.ClientDTO;
import model.entity.Client;
import model.helpers.PaginateHelper;
import services.exceptions.ServiceException;

import java.util.Optional;

/**
 * The {@code ClientServiceI} interface is a service-layer class for manipulating with {@code Client} entity.
 * <p>
 *
 * @author Natalia Makarchuk
 */

public interface ClientServiceI {

    /**
     * Construct data transfer object for {@code Client} with specific id
     *
     * @param id client id in database
     * @return data transfer object contains client properties
     */
    ClientDTO getDTO(int id);

    ClientDTO getByPhone(String phone);

    String getJsonByPhone(String phone);

    String getJsonByPassport(String passport);

    ClientDTO getByPassport(String passport);


    /**
     * Create new {@code Client} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @throws ServiceException if some values are invalid
     */
    Optional<String> create(ClientDTO dto);

    /**
     * Update all fields in corresponding {@code Client} with field values from data transfer object
     *
     * @param dto data transfer object contains client id and properties
     * @throws ServiceException if some values are invalid
     */
    Optional<String> update(ClientDTO dto);

    /**
     * Delete {@code Client} with specific id from database and all its corresponding {@code Contract} and {@code User}
     *
     * @param id id of client to delete
     */
    void delete(int id);

    /**
     * Construct object contains clients in specific range from database and additional info
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with clients in specific range and additional info
     */
    PaginateHelper<Client> getPaginateData(Integer currentPage, int rowPerPage);
}
