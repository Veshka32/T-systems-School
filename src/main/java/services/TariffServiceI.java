package services;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.TariffDAO;
import repositories.TariffOptionDAO;

import java.util.List;
import java.util.stream.Collectors;
public interface TariffServiceI {

    int create(Tariff tariff) throws ServiceException;


    public void delete(int id) throws ServiceException;

    public void update(Tariff tariff) throws ServiceException;

    public Tariff get(int id);

    public Tariff findByName(String name);

    public List<Tariff> getAll();

    public void deleteOption(int tariffId, int optionId) throws ServiceException;
    void addOption(int tariffId, String optionName) throws ServiceException;

    public List<TariffOption> getTariffOptions(int id);

    TariffTransfer getTransfer(int id);

    public List<TariffOption> getAvailableOptions(int id);
}
