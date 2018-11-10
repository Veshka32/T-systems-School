package services.interfaces;

import model.dto.TariffDTO;
import model.entity.Tariff;
import services.ServiceException;

import java.util.List;

public interface TariffServiceI extends GenericServiceI<Tariff> {

    /**
     * Return auto-generated id of created entity
     *
     * @param dto contains state of new {@code Tariff} entity
     * @throws ServiceException if name is not unique or some properties are incompatible with each other
     */
    int create(TariffDTO dto) throws ServiceException;

    void delete(int id) throws ServiceException;

    void update(TariffDTO dto) throws ServiceException;

    List<String> getAllNames();

    /**
     * Build data transfer object for specific {@code Tariff}
     *
     * @param id database id of desired {@code Tariff} object
     * @return {@code TariffDTO} object including only names of all {@code Option} in this {@code Tariff};
     */
    TariffDTO getDto(int id);
}
