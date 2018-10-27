package services;

import config.WebMvcConfig;
import entities.TariffOption;
import entities.TariffOptionDTO;
import entities.TariffOptionTransfer;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.TariffOptionDAO;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private TariffOptionDAO optionDAO;
    private static final Logger logger = Logger.getLogger(WebMvcConfig.class);
    private static final String ERROR_MESSAGE = "Option must not be both mandatory and incompatible";

    @Autowired
    public void setOptionDAO(TariffOptionDAO optionDAO) {
        this.optionDAO = optionDAO;
        optionDAO.setClass(TariffOption.class);
        logger.info("set up optionDAO");
    }

    @Override
    public void create(TariffOption option) throws ServiceException {
        if (optionDAO.isNameExist(option.getName()))
            throw new ServiceException("name is reserved");
        if (option.getIncompatibleOptions().stream().anyMatch(o -> option.getMandatoryOptions().contains(o)))
            throw new ServiceException(ERROR_MESSAGE);
        save(option);
    }

    @Override
    public void save(TariffOption option) {
        option.getIncompatibleOptions().stream()
                .map(o -> optionDAO.findOne(o.getId()))
                .forEach(o -> o.addIncompatibleOption(option));
        optionDAO.save(option);
    }

    @Override
    public void delete(int id) throws ServiceException {
        if (optionDAO.isUsed(id)) throw new ServiceException("Option is used in contracts/tariffs or is mandatory for another option");

        TariffOption option = optionDAO.findOne(id);

        option.getIncompatibleOptions().stream()
                .map(o -> optionDAO.findOne(o.getId()))
                .forEach(o -> o.removeIncompatibleOption(option));
        optionDAO.deleteById(id);
    }

    @Override
    public TariffOption get(int id) {
        return optionDAO.findOne(id);
    }

    @Override
    public TariffOptionTransfer getTransfer(int id) {
        TariffOption option = optionDAO.findOne(id);
        TariffOptionDTO dto = new TariffOptionDTO(option);

        dto.setIncompatible(optionDAO.getIncompatibleOptions(id).stream().map(TariffOption::getId).collect(Collectors.toSet()));
        dto.setMandatory(optionDAO.getMandatoryOptions(id).stream().map(TariffOption::getId).collect(Collectors.toSet()));

        TariffOptionTransfer transfer = new TariffOptionTransfer(dto);
        Map<Integer,String> map=optionDAO.findAll().stream().collect(Collectors.toMap(TariffOption::getId,TariffOption::getName));
        transfer.setAll(map);
        return transfer;
    }

    @Override
    public TariffOption getFull(int id) {
        TariffOption tariffOption = optionDAO.findOne(id);
        Hibernate.initialize(tariffOption.getIncompatibleOptions());
        Hibernate.initialize(tariffOption.getMandatoryOptions());
        return tariffOption;
    }

    @Override
    public TariffOption findByName(String name) {
        return optionDAO.findByName(name);
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
    public void update(TariffOptionDTO dto) throws ServiceException {
        TariffOption o = optionDAO.findByName(dto.getName());

        if (o != null && o.getId() != dto.getId())  //check if there is another option with the same name in database
            throw new ServiceException("name is reserved");

        //check if no option at the same time are mandatory and incompatible

        Optional<Integer> any = dto.getIncompatible().stream().filter(id -> dto.getMandatory().contains(id)).findFirst();
        if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE);

        //check if mandatory options are incompatible with each other
        any = dto.getMandatory().stream().filter(id -> optionDAO.getIncompatibleOptions(id).contains(id)).findFirst();
        if (any.isPresent()) throw new ServiceException("Mandatory options are incompatible");

        //update plain fields
        TariffOption based = optionDAO.findOne(dto.getId());
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setSubscribeCost(dto.getSubscribeCost());
        based.setArchived(dto.isArchived());
        based.setDescription(dto.getDescription());

        //update complex fields
        based.getMandatoryOptions().clear();
        based.getIncompatibleOptions().clear();
        for (Integer id:dto.getIncompatible()) {
            TariffOption newInc=optionDAO.findOne(id);
            based.addIncompatibleOption(newInc);
            newInc.addIncompatibleOption(based);
        }
        for (Integer id:dto.getMandatory()) {
            TariffOption newMand=optionDAO.findOne(id);
            based.addMandatoryOption(newMand);
        }
        optionDAO.update(based);
    }

    @Override
    public void removeIncompatibleOption(int id, int optionId) {
        TariffOption option = optionDAO.findOne(id);
        TariffOption incompatible = optionDAO.findOne(optionId);
        option.removeIncompatibleOption(incompatible);
        incompatible.removeIncompatibleOption(option);
        optionDAO.update(option);
    }

    @Override
    public void addIncompatibleOption(int id, String optionName) throws ServiceException {
        TariffOption option = optionDAO.findOne(id);
        TariffOption incompatible = optionDAO.findByName(optionName);
        if (option.getMandatoryOptions().contains(incompatible) || incompatible.getIncompatibleOptions().contains(option))
            throw new ServiceException(ERROR_MESSAGE);
        option.addIncompatibleOption(incompatible);
        incompatible.addIncompatibleOption(option);
        optionDAO.update(option);
    }

    @Override
    public void removeMandatoryOption(int id, int optionId) {
        TariffOption option = optionDAO.findOne(id);
        TariffOption mandatory = optionDAO.findOne(optionId);
        option.removeMandatoryOption(mandatory);
        optionDAO.update(option);
    }

    @Override
    public void addMandatoryOption(int id, String optionName) throws ServiceException {
        TariffOption option = optionDAO.findOne(id);
        TariffOption mandatory = optionDAO.findByName(optionName);
        if (option.getIncompatibleOptions().contains(mandatory) || mandatory.getIncompatibleOptions().contains(option))
            throw new ServiceException(ERROR_MESSAGE);
        option.addMandatoryOption(mandatory);
        optionDAO.update(option);
    }
}
