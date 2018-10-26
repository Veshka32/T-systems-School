package services;
import config.WebMvcConfig;
import entities.TariffOption;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.GenericDAO;
import repositories.IGenericDAO;

import java.util.List;
import java.util.Set;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private IGenericDAO<TariffOption> optionDAO;
    private static final Logger logger = Logger.getLogger(WebMvcConfig.class);

    @Autowired
    public void setOptionDAO(GenericDAO<TariffOption> optionDAO ) {
        this.optionDAO=optionDAO;
        optionDAO.setClass(TariffOption.class);
        logger.info("set up optionDAO");
    }

    @Override
    public void save(TariffOption option) {
        option.getIncompatibleOptions().stream()
        .map(o->optionDAO.findOne(o.getId()))
        .forEach(o->o.addIncompatibleOption(option));
        optionDAO.save(option);
    }

    @Override
    public void delete(int id) {
        TariffOption option=optionDAO.findOne(id);
        option.getIncompatibleOptions().stream()
                .map(o->optionDAO.findOne(o.getId()))
                .forEach(o->o.removeIncompatibleOption(option));
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
    public void update(TariffOption dto) {
        TariffOption based=optionDAO.findOne(dto.getId());
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setSubscribeCost(dto.getSubscribeCost());
        based.setArchived(dto.isArchived());
        based.setDescription(dto.getDescription());
        optionDAO.update(based);
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
        TariffOption incompatible=optionDAO.findOne(optionId);
        option.removeIncompatibleOption(incompatible);
        incompatible.removeIncompatibleOption(option);
        optionDAO.update(option);}

    @Override
    public void addIncompatibleOption(int id, int optionId) {
        TariffOption option = optionDAO.findOne(id);
        TariffOption incompatible=optionDAO.findOne(optionId);
        option.addIncompatibleOption(incompatible);
        incompatible.addIncompatibleOption(option);
        optionDAO.update(option);
    }

    @Override
    public void archive(int tariffId) {
        TariffOption option = optionDAO.findOne(tariffId);
        option.setArchived(true);
        optionDAO.update(option);
    }

    @Override
    public Set<TariffOption> getIncompatible(int id){
        TariffOption option = optionDAO.findOne(id);
        /**
         * TODO replace with named Query
         */
        Hibernate.initialize(option.getIncompatibleOptions());
        return option.getIncompatibleOptions();
    }
}
