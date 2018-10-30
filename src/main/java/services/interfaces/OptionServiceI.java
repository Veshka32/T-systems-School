package services.interfaces;

import entities.TariffOption;
import entities.TariffOptionTransfer;
import entities.dto.TariffOptionDTO;
import services.ServiceException;

import java.util.List;

public interface OptionServiceI {

    void create(TariffOptionDTO dto) throws ServiceException;

    TariffOption get(int id);

    void update(TariffOptionDTO option) throws ServiceException;

    void delete(int id) throws ServiceException;

    TariffOptionTransfer getTransferForEdit(int id);

    List<TariffOption> getAll();

    List<String> getAllNames();

    TariffOption getFull(int id);
}
