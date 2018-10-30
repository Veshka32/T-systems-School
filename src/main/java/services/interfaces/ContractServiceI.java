package services.interfaces;

import entities.Client;
import entities.Contract;
import entities.TariffOption;
import entities.dto.ContractDTO;
import services.ServiceException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ContractServiceI {
    Client findClientByPhone(long phone);

    Contract findByPhone(long phone);

    Contract get(int id);

    ContractDTO getDTO(int id);

    void create(ContractDTO dto) throws ServiceException;

    void update(ContractDTO dto) throws ServiceException;

    void delete(int id);

    void block(int id);

    void unblock(int id);

    List<Contract> getAllClientContracts(int clientId);

    List<Contract> getAll();

    Contract getFull(int id);

    void addOptions(int id,Collection<TariffOption> options);

    void deleteOption(int id, int optionId) throws ServiceException;

    void setTariff(int id, int tariffId);
}
