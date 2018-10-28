package services;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
import entities.dto.TariffDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.TariffDAO;
import repositories.TariffOptionDAO;

import java.util.List;
import java.util.stream.Collectors;
 public interface TariffServiceI {

    void create(TariffDTO dto) throws ServiceException;

    void delete(int id) throws ServiceException;

     void update(Tariff tariff) throws ServiceException;

     Tariff get(int id);
     Tariff getFull(int id);

     Tariff findByName(String name);

     List<Tariff> getAll();

     void deleteOption(int tariffId, int optionId) throws ServiceException;
    void addOption(int tariffId, String optionName) throws ServiceException;

     List<TariffOption> getTariffOptions(int id);

    TariffTransfer getTransfer(int id);

     List<TariffOption> getAvailableOptions(int id);
}
