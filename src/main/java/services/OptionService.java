package services;
import config.WebMvcConfig;
import entities.TariffOption;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.TariffOptionDAO;

import java.util.List;
import java.util.Set;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private TariffOptionDAO optionDAO;
    private static final Logger logger = Logger.getLogger(WebMvcConfig.class);
    private static final String ERROR_MESSAGE="Option must not be both mandatory and incompatible";

    @Autowired
    public void setOptionDAO(TariffOptionDAO optionDAO ) {
        this.optionDAO=optionDAO;
        optionDAO.setClass(TariffOption.class);
        logger.info("set up optionDAO");
    }

    @Override
    public void create(TariffOption option) throws OptionException {
        if (optionDAO.isNameExist(option.getName()))
            throw new OptionException("name is reserved");
        if (option.getIncompatibleOptions().stream().anyMatch(o->option.getMandatoryOptions().contains(o)))
            throw new OptionException(ERROR_MESSAGE);
        save(option);
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

    public TariffOption getFull(int id){
        TariffOption tariffOption=optionDAO.findOne(id);
        Hibernate.initialize(tariffOption.getIncompatibleOptions());
        Hibernate.initialize(tariffOption.getMandatoryOptions());
        return tariffOption;
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
    public List<String> getAllNames() {
        return optionDAO.getAllNames();
    }

    @Override
    public void update(TariffOption dto) throws OptionException{
        if (optionDAO.isNameExist(dto.getName()))
            throw new OptionException("name is reserved");
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
    public void addIncompatibleOption(int id, String optionName) throws OptionException{
        TariffOption option = optionDAO.findOne(id);
        TariffOption incompatible=optionDAO.findByName(optionName);
        if (option.getMandatoryOptions().contains(incompatible) || incompatible.getIncompatibleOptions().contains(option))
            throw new OptionException(ERROR_MESSAGE);
        option.addIncompatibleOption(incompatible);
        incompatible.addIncompatibleOption(option);
        optionDAO.update(option);
    }

    @Override
    public void removeMandatoryOption(int id, int optionId) {
        TariffOption option = optionDAO.findOne(id);
        TariffOption mandatory=optionDAO.findOne(optionId);
        option.removeMandatoryOption(mandatory);
        optionDAO.update(option);}

    @Override
    public void addMandatoryOption(int id, String optionName) throws OptionException{
        TariffOption option = optionDAO.findOne(id);
        TariffOption mandatory=optionDAO.findByName(optionName);
        if (option.getIncompatibleOptions().contains(mandatory) || mandatory.getIncompatibleOptions().contains(option))
            throw new OptionException(ERROR_MESSAGE);
        option.addMandatoryOption(mandatory);
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
