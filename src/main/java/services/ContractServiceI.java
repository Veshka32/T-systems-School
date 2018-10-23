package services;

import entities.Contract;

public interface ContractServiceI {

    Contract findByPhone(int phone);

    Contract create(Contract contract);
}
