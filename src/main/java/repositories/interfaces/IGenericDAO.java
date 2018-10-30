package repositories.interfaces;

import java.io.Serializable;
import java.util.List;
public interface IGenericDAO<T> {

        T findOne(final int id);

        List<T> findAll();

        int save(final T entity);

        void update(final T entity);

        void delete(final T entity);

        void deleteById(final int entityId);

        void setClass( Class< T > clazzToSet);

}
