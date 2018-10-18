package services;

import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.GenericDAO;
import repositories.IGenericDAO;

import java.util.List;

@Service
@EnableTransactionManagement
public class TariffService implements TariffServiceI {

    IGenericDAO<Tariff> tariffDAO;
    IGenericDAO<TariffOption> optionDAO;

    @Autowired
    public void setTariffDAO(GenericDAO<Tariff> tariffDAO,GenericDAO<TariffOption> optionDAO ) {
        this.tariffDAO = tariffDAO;
        this.optionDAO=optionDAO;
        tariffDAO.setClass(Tariff.class);
        optionDAO.setClass(TariffOption.class);
    }

    @Override
    @Transactional
    public void create(String name) {
        tariffDAO.create(new Tariff(name));
    }

    @Override
    public void delete(int id) {
        tariffDAO.deleteById(id);
    }

    @Override
    public Tariff get(int id) {
        return tariffDAO.findOne(id);
    }

    @Override
    public Tariff findByName(String name) {
        return tariffDAO.findByNaturalId(name);
    }

    @Override
    @Transactional
    public List<Tariff> getAll() {
        return tariffDAO.findAll();
    }

    @Override
    public void addBaseOptions(int tariffId, int... optionId) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        for (int id : optionId) {
            tariff.addBaseOption(optionDAO.findOne(id));
        }
        tariffDAO.update(tariff);
    }

    @Override
    public void deleteBaseOptions(int tariffId, int... optionId) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        for (int id : optionId
                ) {
            tariff.deleteBaseOption(optionDAO.findOne(id));
        }
        tariffDAO.update(tariff);
    }

    @Override
    public void updatePrice(int tariffId, int price) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        tariff.setPrice(price);
        tariffDAO.update(tariff);
    }

    @Override
    public void setIncompatibleOptions(int tariffId, int... optionId) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        for (int id : optionId) {
            tariff.setIncompatibleOption(optionDAO.findOne(id));
        }
        tariffDAO.update(tariff);
    }

    @Override
    public void removeIncompatibleOptions(int tariffId, int... optionId) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        for (int id : optionId) {
            tariff.deleteIncompatibleOption(optionDAO.findOne(id));
        }
        tariffDAO.update(tariff);
    }

    @Override
    public void setIncompatibleTafiff(int tariffId, int... tariff2Id) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        for (int id : tariff2Id) {
            tariff.setIncompatibleTariffs(tariffDAO.findOne(id));
        }
        tariffDAO.update(tariff);
    }

    @Override
    public void removeIncompatibleTariff(int tariffId, int... tariff2Id) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        for (int id : tariff2Id) {
            tariff.deleteIncompatibleTariff(tariffDAO.findOne(id));
        }
        tariffDAO.update(tariff);
    }

    @Override
    public void archive(int tariffId) {
        Tariff tariff = tariffDAO.findOne(tariffId);
        tariff.archive();
        tariffDAO.update(tariff);
    }
}
