package services;
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
@Transactional
public class OptionService implements OptionServiceI {

    private IGenericDAO<TariffOption> optionDAO;

    @Autowired
    public void setOptionDAO(GenericDAO<TariffOption> optionDAO ) {
        this.optionDAO=optionDAO;
        optionDAO.setClass(TariffOption.class);
    }

    @Override
    public void save(TariffOption option) {
        optionDAO.save(option);
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
    public TariffOption findByName(String name) {
        return optionDAO.findByNaturalId(name);
    }

    @Override
    public List<TariffOption> getAll() {
        return optionDAO.findAll();
    }

    @Override
    public void update(TariffOption option) {
        optionDAO.update(option);
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
        option.setSubscribeCost(cost);
        optionDAO.update(option);
    }

    @Override
    public void removeIncompatibleOption(int id, int optionId) {
        TariffOption option = optionDAO.findOne(id);
        option.addIncompatibleOption(optionDAO.findOne(optionId));
        optionDAO.update(option);
    }

    @Override
    public void archive(int tariffId) {
        TariffOption option = optionDAO.findOne(tariffId);
        option.setArchived(true);
        optionDAO.update(option);
    }
}
