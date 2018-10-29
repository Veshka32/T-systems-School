package services;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
import entities.dto.TariffDTO;
import java.util.List;
 public interface TariffServiceI {

    void create(TariffDTO dto) throws ServiceException;

    void delete(int id) throws ServiceException;

     void update(TariffDTO dto) throws ServiceException;

     Tariff get(int id);
     Tariff getFull(int id);

     Tariff findByName(String name);

     List<Tariff> getAll();
     List<String> getAllNames();

    TariffTransfer getTransferForEdit(int id);
}
