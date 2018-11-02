package repositories.interfaces;

import entities.OptionRelation;


public interface RelationDaoI extends IGenericDAO<OptionRelation> {
    void deleteAllIncompatible(int id);

    void deleteAllMandatory(int id);
}
