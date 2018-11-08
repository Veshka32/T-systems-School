package services.interfaces;

import entities.Option;
import entities.dto.OptionDTO;
import services.ServiceException;

import java.util.List;

public interface OptionServiceI extends GenericServiceI<Option> {

    /**
     * Return auto-generated id of created entity
     *
     * @param dto contains state of new {@code Option} entity
     * @throws ServiceException if name is not unique or some properties are incompatible with each other
     */
    int create(OptionDTO dto) throws ServiceException;

    OptionDTO getDto(int id);

    void update(OptionDTO option) throws ServiceException;

    void delete(int id) throws ServiceException;

    List<String> getAllNames();
}
