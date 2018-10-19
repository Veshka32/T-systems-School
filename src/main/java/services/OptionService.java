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
public class OptionService implements OptionServiceI {

    IGenericDAO<TariffOption> optionDAO;

    @Autowired
    public void setOptionDAO(GenericDAO<TariffOption> optionDAO ) {
        this.optionDAO=optionDAO;
        optionDAO.setClass(TariffOption.class);
    }

    @Override
    @Transactional
    public void create(String name,int price) {
        optionDAO.create(new TariffOption(name,price));
    }

    @Override
    public void delete(int id) {
        optionDAO.deleteById(id);
    }

    @Override
    public TariffOption get(int id) {
        return optionDAO.findOne(id);
    }

    @Override
    @Transactional
    public TariffOption findByName(String name) {
        return optionDAO.findByNaturalId(name);
    }

    @Override
    @Transactional
    public List<TariffOption> getAll() {
        return optionDAO.findAll();
    }

    @Override
    public void updatePrice(int id, int price) {
        TariffOption option = optionDAO.findOne(id);
        option.setPrice(price);
        optionDAO.update(option);
    }

    @Override
    public void updateSubscribeCost(int id,int cost) {
        TariffOption option = optionDAO.findOne(id);
        option.setSubscriveCost(cost);
        optionDAO.update(option);
    }

    @Override
    public void setIncompatibleOptions(int id, int... optionId) {
        TariffOption tariff = optionDAO.findOne(id);
        for (int o : optionId) {
            tariff.addIncompatibleOptions(optionDAO.findOne(o));
        }
        optionDAO.update(tariff);
    }

    @Override
    public void removeIncompatibleOptions(int id, int... optionId) {
        TariffOption option = optionDAO.findOne(id);
        for (int o : optionId) {
            option.removeIncompatibleOptions(optionDAO.findOne(id));
        }
        optionDAO.update(option);
    }

    @Override
    public void archive(int tariffId) {
        TariffOption option = optionDAO.findOne(tariffId);
        option.setArchived();
        optionDAO.update(option);
    }
}
