package services;

import entities.TariffOption;
import entities.TariffOptionDTO;
import entities.TariffOptionTransfer;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.TariffOptionDAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private TariffOptionDAO optionDAO;
    private static final String ERROR_MESSAGE = "Option must not be both mandatory and incompatible";
    private static final String ERROR_MESSAGE1="Mandatory options are incompatible";

    @Autowired
    public void setOptionDAO(TariffOptionDAO optionDAO) {
        this.optionDAO = optionDAO;
        optionDAO.setClass(TariffOption.class);
    }

    @Override
    public void create(TariffOptionDTO dto) throws ServiceException {
        if (optionDAO.isNameExist(dto.getName()))
            throw new ServiceException("name is reserved");

        //check if no option at the same time are mandatory and incompatible
        Optional<String> any = dto.getIncompatible().stream().filter(name -> dto.getMandatory().contains(name)).findFirst();
        if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE);

        //check if mandatory options are incompatible with each other
        for (String mandatory:dto.getMandatory()) {
            TariffOption m=optionDAO.findByName(mandatory);
            Set<TariffOption> incompatible=m.getIncompatibleOptions(); //how to get only ids?
            any=incompatible.stream().map(o->o.getName()).filter(o->dto.getMandatory().contains(o)).findAny();
            if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE1);
        }

        //set plain fields
        TariffOption based = new TariffOption();
        updatePlainFields(dto,based);

        //set collections fields
        for (String name:dto.getIncompatible()) {
            TariffOption newIncompatible=optionDAO.findByName(name);
            based.addIncompatibleOption(newIncompatible);
            newIncompatible.addIncompatibleOption(based);
        }
        for (String name:dto.getMandatory()) {
            TariffOption newMandatory=optionDAO.findByName(name);
            based.addMandatoryOption(newMandatory);
        }
        save(based);
        dto.setId(based.getId());
    }

    @Override
    public void update(TariffOptionDTO dto) throws ServiceException {
        TariffOption op = optionDAO.findByName(dto.getName());

        if (op != null && op.getId() != dto.getId())  //check if there is another option with the same name in database
            throw new ServiceException("name is reserved");

        //check if no option at the same time are mandatory and incompatible
        Optional<String> any = dto.getIncompatible().stream().filter(name -> dto.getMandatory().contains(name)).findFirst();
        if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE);

        //check if mandatory options are incompatible with each other
        for (String mandatory:dto.getMandatory()) {
            TariffOption m=optionDAO.findByName(mandatory);
            Set<TariffOption> incompatible=m.getIncompatibleOptions(); //how to get only ids?
            any=incompatible.stream().map(TariffOption::getName).filter(o->dto.getMandatory().contains(o)).findAny();
            if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE1);
        }

        //check if option is mandatory for some incompatible
        for (String incom:dto.getIncompatible()){
            TariffOption m=optionDAO.findByName(incom);
            Set<TariffOption> mandatory=m.getMandatoryOptions();
            if (mandatory.contains(op)) throw new ServiceException("This option is mandatory for "+m.getName());
        }

        //update plain fields
        TariffOption based = optionDAO.findOne(dto.getId());
        updatePlainFields(dto,based);

        //update complex fields
        based.getMandatoryOptions().clear();
        based.getIncompatibleOptions().clear();
        for (String name:dto.getIncompatible()) {
            TariffOption newIncompatible=optionDAO.findByName(name);
            based.addIncompatibleOption(newIncompatible);
            newIncompatible.addIncompatibleOption(based);
        }
        for (String name:dto.getMandatory()) {
            TariffOption newMandatory=optionDAO.findByName(name);
            based.addMandatoryOption(newMandatory);
        }
        optionDAO.update(based);
    }

    private void updatePlainFields(TariffOptionDTO dto,TariffOption based){
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setSubscribeCost(dto.getSubscribeCost());
        based.setArchived(dto.isArchived());
        based.setDescription(dto.getDescription());
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
    public TariffOption getById(int id) {
        return optionDAO.findOne(id);
    }

    @Override
    public TariffOptionTransfer getTransferForEdit(int id) {
        TariffOption option = optionDAO.findOne(id);
        TariffOptionDTO dto = new TariffOptionDTO(option);

        dto.setIncompatible(option.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.toSet())); //how to get only ids?
        dto.setMandatory(option.getMandatoryOptions().stream().map(TariffOption::getName).collect(Collectors.toSet()));

        TariffOptionTransfer transfer = new TariffOptionTransfer(dto);
        Map<Integer,String> map=getAllNames();
        map.remove(id);
        transfer.setAll(map);
        return transfer;
    }

    @Override
    public Map<Integer,String> getAllNames() {
        Map<Integer,String> map=optionDAO.findAll().stream().collect(Collectors.toMap(TariffOption::getId,TariffOption::getName));
        return map;
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
}
