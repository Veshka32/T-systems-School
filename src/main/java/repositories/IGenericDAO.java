package repositories;

import java.io.Serializable;
import java.util.List;
public interface IGenericDAO<T extends Serializable> {

        T findOne(final int id);

        T findByNaturalId(final String naturalKey);

        List<T> findAll();

        void create(final T entity);

        void update(final T entity);

        void delete(final T entity);

        void deleteById(final int entityId);

        void setClass( Class< T > clazzToSet);

}
