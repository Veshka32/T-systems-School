package services.interfaces;

import model.dto.TariffDTO;
import model.entity.Tariff;
import model.helpers.PaginateHelper;

import java.util.List;
import java.util.Optional;

public interface TariffServiceI {

    /**
     * Create new {@code Tariff} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return database id of newly created entity
     */
    Optional<String> create(TariffDTO dto);

    /**
     * Delete {@code Tariff} with specific id from database
     *
     * @param id id of tariff to delete
     */
    Optional<String> delete(int id);

    /**
     * Update all fields in corresponding {@code Tariff} with field values from data transfer object
     *
     * @param dto data transfer object contains tariff id and properties
     */
    Optional<String> update(TariffDTO dto);

    /**
     * Build data transfer object for tariff with specific id
     *
     * @param id database id of desired {@code Tariff} object
     * @return {@code TariffDTO} object contains contract properties
     */
    TariffDTO getDto(int id);

    /**
     * Construct object contains tariffs in specific range from database and additional info
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with tariffs in specific range and additional info
     */

    PaginateHelper<Tariff> getPaginateData(Integer currentPage, int rowPerPage);

    List<Tariff> getLast(int count);
}
