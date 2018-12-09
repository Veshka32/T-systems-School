package services.interfaces;

import model.dto.OptionDTO;
import model.entity.Option;
import model.helpers.PaginateHelper;

import java.util.Map;
import java.util.Optional;

public interface OptionServiceI {

    /**
     * Create new {@code Option} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return database id of newly created entity
     */
    Optional<String> create(OptionDTO dto);

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
     */
    Optional<String> update(OptionDTO dto);

    /**
     * Delete {@code Option} with specific id from database and its corresponding {@code OptionRelation}
     *
     * @param id id of option to delete
     */
    Optional<String> delete(int id);

    /**
     * Build map from names and corresponding ids of all options in database
     *
     * @return names and corresponding ids of all {@code Option} in database
     */
    Map<String, Integer> getAllNamesWithIds();

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
