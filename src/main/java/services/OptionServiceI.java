package services;

import entities.TariffOption;
import entities.TariffOptionDTO;
import entities.TariffOptionTransfer;

import java.util.List;
import java.util.Map;

public interface OptionServiceI {


    void create(TariffOptionDTO dto) throws ServiceException;

    void save(TariffOption option);

    void delete(int id) throws ServiceException;

    TariffOption getById(int id);

    TariffOptionTransfer getTransferForEdit(int id);
    Map<Integer,String> getAllNames();

    TariffOption getFull(int id);

    TariffOption findByName(String name);

    List<TariffOption> getAll();

    void update(TariffOptionDTO option) throws ServiceException;
}
