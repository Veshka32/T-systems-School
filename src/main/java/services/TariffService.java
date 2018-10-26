package services;

import entities.Tariff;
import entities.TariffOption;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.GenericDAO;
import repositories.IGenericDAO;
import repositories.TariffDAO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class TariffService {

    private TariffDAO tariffDAO;
    private IGenericDAO<TariffOption> optionDAO;

    @Autowired
    public void setTariffDAO(TariffDAO tariffDAO,GenericDAO<TariffOption> optionDAO ) {
        this.tariffDAO = tariffDAO;
        this.optionDAO=optionDAO;
        tariffDAO.setClass(Tariff.class);
        optionDAO.setClass(TariffOption.class);
    }

    @Transactional
    public int create(Tariff tariff) {
        return tariffDAO.save(tariff);
    }

    @Transactional
    public void delete(int id) {
        tariffDAO.deleteById(id);
    }

    @Transactional
    public void update(Tariff tariff){
        tariffDAO.update(tariff);
    }

    @Transactional
    public Tariff get(int id) {
        return tariffDAO.findOne(id);
    }

    @Transactional
    public Tariff findByName(String name) {
        return tariffDAO.findByNaturalId(name);
    }

    @Transactional
    public List<Tariff> getAll() {
        return tariffDAO.findAll();
    }

    @Transactional
    public void deleteOption(int tariffId, int optionId) {
        Tariff tariff = tariffDAO.findOne(tariffId);
            tariff.deleteOption(optionDAO.findOne(optionId));
        tariffDAO.update(tariff);
    }

    @Transactional
    public List<TariffOption> getTariffOptions(int id){
        return tariffDAO.getOptions(id);
    }

    @Transactional
    public List<TariffOption> getAvailableOptions(int id){
        List<TariffOption> tariffOptions=getTariffOptions(id);
        return optionDAO.findAll().stream()
                .filter(o->!tariffOptions.contains(o)).collect(Collectors.toList());
    }
}
