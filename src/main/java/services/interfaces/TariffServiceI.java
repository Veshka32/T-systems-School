package services.interfaces;

import entities.Tariff;
import entities.TariffTransfer;
import entities.dto.TariffDTO;
import services.ServiceException;

import java.util.List;

public interface TariffServiceI {

    void create(TariffDTO dto) throws ServiceException;

    void delete(int id) throws ServiceException;

    void update(TariffDTO dto) throws ServiceException;

    Tariff get(int id);

    List<Tariff> getAll();

    List<String> getAllNames();

    TariffTransfer getTransferForEdit(int id);

    TariffDTO getDto(int id);
}
