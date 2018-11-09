package services.interfaces;

import entities.helpers.PaginateHelper;

import java.util.List;

public interface GenericServiceI<T> {
    /**
     * Return {@code T} entity from database with given id.
     *
     * @param id database id of desired entity
     * @return {@code T} found by given id or {@code null} if there is no entity with given id
     */
    T get(int id);

    List<T> getAll();

    PaginateHelper<T> getPaginateData(Integer currentPage, int rowPerPage);
}
