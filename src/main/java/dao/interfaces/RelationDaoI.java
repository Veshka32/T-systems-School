package dao.interfaces;

import model.entity.OptionRelation;


public interface RelationDaoI extends IGenericDAO<OptionRelation> {
    void deleteAllIncompatible(int id);

    void deleteAllMandatory(int id);
}
