package services.interfaces;

import entities.Client;
import entities.Contract;
import entities.Option;
import entities.dto.ContractDTO;
import services.ServiceException;

import java.util.Collection;
import java.util.List;

public interface ContractServiceI extends GenericServiceI<Contract> {
    Client findClientByPhone(long phone);

    Contract findByPhone(long phone);

    ContractDTO getDTO(int id);

    void create(ContractDTO dto) throws ServiceException;

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
