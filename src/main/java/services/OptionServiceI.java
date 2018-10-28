package services;

import entities.TariffOption;
import entities.TariffOptionDTO;
import entities.TariffOptionTransfer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OptionServiceI {


    void create(TariffOptionDTO dto) throws ServiceException;

    void save(TariffOption option);

    void delete(int id) throws ServiceException;

    TariffOptionTransfer getTransferForEdit(int id);
    List<String> getAllNames();

    TariffOption getFull(int id);

    TariffOption findByName(String name);

    List<TariffOption> getAll();

    void update(TariffOptionDTO option) throws ServiceException;
}
