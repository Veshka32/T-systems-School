package services.interfaces;

import model.dto.TariffDTO;
import model.entity.Tariff;
import model.helpers.PaginateHelper;
import services.exceptions.ServiceException;

import java.util.List;
import java.util.Map;

public interface TariffServiceI {

    /**
     * Create new {@code Tariff} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return database id of newly created entity
     * @throws ServiceException if some values are invalid or some properties has conflict
     */
    int create(TariffDTO dto) throws ServiceException;

    /**
     * Delete {@code Tariff} with specific id from database
     *
     * @param id id of tariff to delete
     * @throws ServiceException if tariff is used in any database relations
     */
    void delete(int id) throws ServiceException;

    /**
     * Update all fields in corresponding {@code Tariff} with field values from data transfer object
     *
     * @param dto data transfer object contains tariff id and properties
     * @throws ServiceException if some values are invalid or some properties has conflict
     */
    void update(TariffDTO dto) throws ServiceException;

    /**
     * Build data transfer object for tariff with specific id
     *
     * @param id database id of desired {@code Tariff} object
     * @return {@code TariffDTO} object contains contract properties
     */
    TariffDTO getDto(int id);

    /**
     * Get all tariffs from database
     *
     * @return list with all {@code Tariff} from database
     */
    List<Tariff> getAll();

    /**
     * Build map from names and corresponding ids of all options in database
     *
     * @return names and corresponding ids of all {@code Option} in database
     */
    Map<String, Integer> getAllNamesWithIds();

    /**
     * Construct object contains tariffs in specific range from database and additional info
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with tariffs in specific range and additional info
     */

    PaginateHelper<Tariff> getInRange(Integer currentPage, int rowPerPage);

    List<Tariff> getLast(int count);
}
