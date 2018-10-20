package repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Repository
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GenericDAO<T extends Serializable> implements IGenericDAO<T> {

    @Autowired
    SessionFactory sessionFactory;
    private Class<T> clazz;


    @Override
    public final void setClass(Class<T> clazzToSet) {

        //this.clazz =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.clazz = clazzToSet;
    }

    @Override
    public T findOne(int id) {
        return sessionFactory.getCurrentSession().get(clazz, id);
    }

    @Override
    public T findByNaturalId(String naturalKey) {
        return sessionFactory.getCurrentSession().bySimpleNaturalId(clazz).load(naturalKey);
    }

    @Override
    public List<T> findAll() {
        Session session=sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(clazz);
        Root<T> root = cr.from(clazz);
        cr.select(root);
        Query<T> query = session.createQuery(cr);
        return query.getResultList();
    }

    @Override
    public void create(T entity) {
        sessionFactory.getCurrentSession().persist(entity);
    }

    @Override
    public void update(T entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public void deleteById(int entityId) {
        T entity = findOne(entityId);
        delete(entity);
    }
}
