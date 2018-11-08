package services.interfaces;

import entities.Tariff;
import entities.dto.TariffDTO;
import entities.dto.TariffTransfer;
import services.ServiceException;

import java.util.List;

public interface TariffServiceI extends GenericServiceI<Tariff> {

    void create(TariffDTO dto) throws ServiceException;

    void delete(int id) throws ServiceException;

    void update(TariffDTO dto) throws ServiceException;

    List<String> getAllNames();

    TariffTransfer getTransferForEdit(int id);

    TariffDTO getDto(int id);
}
