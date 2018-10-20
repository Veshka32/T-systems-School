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

    private IGenericDAO<Tariff> tariffDAO;
    private IGenericDAO<TariffOption> optionDAO;

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
    @Transactional
    public void delete(int id) {
        tariffDAO.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Tariff tariff){
        tariffDAO.update(tariff);
    }

    @Override
    @Transactional
    public Tariff get(int id) {
        return tariffDAO.findOne(id);
    }

    @Override
    @Transactional
    public Tariff findByName(String name) {
        return tariffDAO.findByNaturalId(name);
    }

    @Override
    @Transactional
    public List<Tariff> getAll() {
        return tariffDAO.findAll();
    }

    @Override
    @Transactional
    public void deleteOption(int tariffId, int optionId) {
        Tariff tariff = tariffDAO.findOne(tariffId);

            tariff.deleteOption(optionDAO.findOne(optionId));

        tariffDAO.update(tariff);
    }

    @Override
    @Transactional
    public void deleteIncompatibleOption(int tariffId, int optionId) {
        Tariff tariff = tariffDAO.findOne(tariffId);

            tariff.deleteIncompatibleOption(optionDAO.findOne(optionId));

        tariffDAO.update(tariff);
    }
}
