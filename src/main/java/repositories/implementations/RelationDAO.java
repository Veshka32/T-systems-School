package repositories.implementations;

import entities.OptionRelation;
import org.springframework.stereotype.Repository;
import repositories.interfaces.RelationDaoI;

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
