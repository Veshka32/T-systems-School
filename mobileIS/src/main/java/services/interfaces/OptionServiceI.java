package services.interfaces;

import model.dto.OptionDTO;
import model.entity.Option;
import model.helpers.PaginateHelper;
import services.exceptions.ServiceException;

import java.util.List;
import java.util.Map;

public interface OptionServiceI {

    /**
     * Create new {@code Option} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return database id of newly created entity
     * @throws ServiceException if some values are invalid or some properties has conflict
     */
    int create(OptionDTO dto) throws ServiceException;

    /**
     * Build data transfer object for option with specific id
     *
     * @param id database id of desired {@code Option} object
     * @return {@code OptionDTO} object contains contract properties
     */
    OptionDTO getDto(int id);

    /**
     * Update all fields in corresponding {@code Option} with field values from data transfer object
     *
     * @param dto data transfer object contains option id and properties
     * @throws ServiceException if some values are invalid or some properties has conflict
     */
    void update(OptionDTO dto) throws ServiceException;

    /**
     * Delete {@code Option} with specific id from database and its corresponding {@code OptionRelation}
     *
     * @param id id of option to delete
     * @throws ServiceException if option is used in any database relations
     */
    void delete(int id) throws ServiceException;

    /**
     * Build map from names and corresponding ids of all options in database
     *
     * @return names and corresponding ids of all {@code Option} in database
     */
    Map<String, Integer> getAllNamesWithIds();

    /**
     * @return all {@code Option} from database
     */

    List<Option> getAll();

    /**
     * Find option with specific id
     *
     * @param id id of {@code Option}
     * @return {@code Option} with specific id
     */
    Option get(int id);

    /**
     * Construct object contains option in specific range from database and additional info
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with options in specific range and additional info
     */
    PaginateHelper<Option> getPaginateData(Integer currentPage, int rowPerPage);
}
