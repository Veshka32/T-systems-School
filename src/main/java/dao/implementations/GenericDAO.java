package dao.implementations;

import dao.interfaces.IGenericDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenericDAO<T> implements IGenericDAO<T> {

    @Autowired
    SessionFactory sessionFactory;
    private Class<T> clazz;


    @Override
    public final void setClass(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public Long count() {
        Query query = sessionFactory.getCurrentSession().createQuery("select count(*) from " + clazz.getName());
        return (Long) query.uniqueResult();

    }

    @Override
    public T findOne(int id) {
        return sessionFactory.getCurrentSession().get(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from " + clazz.getName(), clazz).list();
    }

    @Override
    public List<T> allInRange(int offset, int limit) {
        Query<T> query = sessionFactory.getCurrentSession().createQuery("From " + clazz.getName(), clazz);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
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
