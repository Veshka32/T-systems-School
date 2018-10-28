package services;

import entities.TariffOption;
import entities.dto.TariffOptionDTO;
import entities.TariffOptionTransfer;

import java.util.List;

public interface OptionServiceI {


    void create(TariffOptionDTO dto) throws ServiceException;

    void delete(int id) throws ServiceException;

    TariffOptionTransfer getTransferForEdit(int id);
    List<String> getAllNames();
    List<String> getAllActiveNames();

    TariffOption getFull(int id);

    TariffOption findByName(String name);

    List<TariffOption> getAll();

    void update(TariffOptionDTO option) throws ServiceException;
}
