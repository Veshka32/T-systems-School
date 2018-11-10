package dao.implementations;

import dao.interfaces.RelationDaoI;
import model.entity.OptionRelation;
import org.springframework.stereotype.Repository;

@Repository
public class RelationDAO extends GenericDAO<OptionRelation> implements RelationDaoI {


    @Override
    public void deleteAllIncompatible(int id) {
        sessionFactory.getCurrentSession()
                .createNamedQuery("delete_incompatible")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteAllMandatory(int id) {
        sessionFactory.getCurrentSession()
                .createNamedQuery("delete_mandatory")
                .setParameter("id", id)
                .executeUpdate();
    }


}
