package services.interfaces;

import entities.Client;
import entities.Contract;
import entities.dto.ContractDTO;
import services.ServiceException;

import java.util.List;

public interface ContractServiceI {
    Client findClientByPhone(long phone);

    Contract findByPhone(long phone);

    Contract get(int id);

    ContractDTO getDTO(int id);

    void create(ContractDTO dto) throws ServiceException;

    void update(ContractDTO dto) throws ServiceException;

    void delete(int id);

    void block(long phone);

    void unblock(long phone);

    List<Contract> getAllClientContracts(int clientId);

    List<Contract> getAll();

    Contract getFull(long phone);

    void deleteOption(long phone, int optionId) throws ServiceException;

    void setTariff(long phone, int tariffId);
}
