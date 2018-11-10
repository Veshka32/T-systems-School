package services.interfaces;

import model.dto.ContractDTO;
import model.entity.Client;
import model.entity.Contract;
import model.entity.Option;
import services.ServiceException;

import java.util.Collection;
import java.util.List;

public interface ContractServiceI extends GenericServiceI<Contract> {
    Client findClientByPhone(long phone);

    Integer findByPhone(long phone);

    /**
     * Build data transfer object for specific {@code Contract}
     *
     * @param id database id of desired {@code Contract} object
     * @return {@code ContractDTO} object including only names of all {@code Option} in this {@code Contract};
     */
    ContractDTO getDTO(int id);

    /**
     * Return auto-generated id of created entity
     *
     * @param dto contains state of new {@code Contract} entity
     * @throws ServiceException if some {@code Option}s are incompatible with each other
     */
    int create(ContractDTO dto) throws ServiceException;

    void update(ContractDTO dto) throws ServiceException;

    void delete(int id);

    void block(int id);

    void unblock(int id);

    List<Contract> getAllClientContracts(int clientId);

    Contract getFull(int id);

    void addOptions(int id, Collection<Option> options) throws ServiceException;

    void deleteOption(int id, int optionId) throws ServiceException;

    void setTariff(int id, int tariffId);
}
