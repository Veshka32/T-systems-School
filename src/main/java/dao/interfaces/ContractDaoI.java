package dao.interfaces;

import entities.Client;
import entities.Contract;

import java.util.List;

public interface ContractDaoI extends IGenericDAO<Contract> {
    List<Contract> getClientContracts(int clientId);

    Client findClientByPhone(long phone);

    Contract findByPhone(long phone);
}
