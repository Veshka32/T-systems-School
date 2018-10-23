package services;

import entities.Contract;

public interface ContractServiceI {

    Contract findByPhone(int phone);

    void create(Contract contract);
}
