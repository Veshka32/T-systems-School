package services.interfaces;

import model.dto.ContractDTO;
import model.entity.Contract;
import model.entity.Option;
import model.helpers.PaginateHelper;
import model.stateful.CartInterface;

import java.util.Collection;
import java.util.Optional;

public interface ContractServiceI {


    /**
     * Return id of {@code Contract} with specific phone number
     *
     * @param phone phone number
     * @return id of Contract or {@code null} if there is no such contract
     */
    String getJsonByPhone(String phone);

    /**
     * Return contract with specific id
     *
     * @param id database id of contract
     * @return {@code Contract} with specific id
     */
    Contract get(int id);

    /**
     * Build data transfer object for {@code Contract} with specific id
     *
     * @param id database id of desired {@code Contract} object
     * @return {@code ContractDTO} object contains contract properties
     */
    ContractDTO getDTO(int id);

    void addData(ContractDTO dto);

    /**
     * Create new {@code Contract} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return database id of newly created entity
     */
    Optional<String> create(ContractDTO dto);

    /**
     * Update all fields in corresponding {@code Contract} with field values from data transfer object
     *
     * @param dto data transfer object contains contract id and properties
     */
    Optional<String> update(ContractDTO dto);

    /**
     * Delete {@code Contract} with specific id from database and its corresponding {@code User}
     *
     * @param id id of contract to delete
     */
    void delete(int id);

    /**
     * Set block property of specific contract to true;
     *
     * @param id id of {@code Contract} to block
     */
    void block(int id);

    /**
     * Set block property of specific contract to false;
     *
     * @param id id of {@code Contract}  to unblock
     */
    void unblock(int id);

    /**
     * Return contract with eager fetched collection fields (no proxies)
     *
     * @param id id {@code Contract}
     * @return {@code Contract} with fully loaded fields from database
     */
    Contract getFull(int id);

    /**
     * Return contract with eager fetched collection fields (no proxies)
     *
     * @param id id {@code Contract}
     * @return {@code Contract} with fully loaded fields from database
     */
    Contract getFullByPhone(long phone);

    /**
     * Add options into the specific contract
     *
     * @param id      id of {@code Contract}
     * @param options collection of {@code Option} to add
     */
    Optional<String> addOptions(int id, Collection<Option> options);

    String addOptionsToJson(CartInterface cart);

    /**
     * Delete option from specific contract
     *
     * @param id       id of {@code Contract}
     * @param optionId id of {@code Option} to delete
     */
    Optional<String> deleteOption(int id, int optionId);

    String deleteOptionJson(int id, int optionId);

    /**
     * Replace tariff in contract to the new one
     *
     * @param id       id of {@code Contract}
     * @param tariffId id of {@code Tariff} to set
     */
    Optional<String> setTariff(int id, int tariffId);


    /**
     * Construct object contains contract in specific range from database and additional info
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with contracts in specific range and additional info
     */
    PaginateHelper<Contract> getPaginateData(Integer currentPage, int rowPerPage);
}
