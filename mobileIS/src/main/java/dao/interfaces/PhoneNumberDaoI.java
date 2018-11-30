package dao.interfaces;

import model.entity.NumberGenerator;

public interface PhoneNumberDaoI extends IGenericDAO<NumberGenerator> {
    NumberGenerator getForUpdate(int id);
}
