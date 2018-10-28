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
public class GenericDAO<T> implements IGenericDAO<T> {

    @Autowired
    SessionFactory sessionFactory;
    private Class<T> clazz;


    @Override
    public final void setClass(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public T findOne(int id) {
        return sessionFactory.getCurrentSession().get(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from "+clazz.getName()).list();
    }

    @Override
    public int save(T entity) {
        return (int)sessionFactory.getCurrentSession().save(entity);
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
    }}
