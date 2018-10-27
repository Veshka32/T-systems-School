package services;

import entities.TariffOption;
import entities.TariffOptionDTO;
import entities.TariffOptionTransfer;

import java.util.List;

public interface OptionServiceI {


    void create(TariffOption option) throws ServiceException;

    void save(TariffOption option);

    void delete(int id) throws ServiceException;

    TariffOption getById(int id);

    TariffOptionTransfer getTransfer(int id);

    TariffOption getFull(int id);

    TariffOption findByName(String name);

    List<TariffOption> getAll();

    void update(TariffOptionDTO option) throws ServiceException;
}
