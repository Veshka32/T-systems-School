package dao.interfaces;

import model.entity.Client;
import model.entity.Contract;

import java.util.List;

public interface ContractDaoI extends IGenericDAO<Contract> {
    List<Contract> getClientContracts(int clientId);

    Client findClientByPhone(long phone);

    Integer getIdByPhone(long phone);

    Contract findByPhone(long phone);
}
