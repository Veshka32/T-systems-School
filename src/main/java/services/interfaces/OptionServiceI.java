package services.interfaces;

import entities.Option;
import entities.dto.OptionDTO;
import entities.dto.OptionTransfer;
import services.ServiceException;

import java.util.List;

public interface OptionServiceI {

    void create(OptionDTO dto) throws ServiceException;

    Option get(int id);

    void update(OptionDTO option) throws ServiceException;

    void delete(int id) throws ServiceException;

    OptionTransfer getTransferForEdit(int id);

    List<Option> getAll();

    List<String> getAllNames();

    OptionDTO getFull(int id);
}
