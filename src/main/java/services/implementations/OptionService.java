package services.implementations;

import entities.TariffOption;
import entities.dto.TariffOptionDTO;
import entities.TariffOptionTransfer;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.interfaces.TariffOptionDaoI;
import services.ServiceException;
import services.interfaces.OptionServiceI;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private TariffOptionDaoI optionDAO;
    private static final String ERROR_MESSAGE = "Option must not be both mandatory and incompatible";

    @Autowired
    public void setOptionDAO(TariffOptionDaoI optionDAO) {
        this.optionDAO = optionDAO;
        optionDAO.setClass(TariffOption.class);
    }

    @Override
    public TariffOption get(int id) {
        return optionDAO.findOne(id);
    }

    @Override
    public void create(TariffOptionDTO dto) throws ServiceException {
        if (optionDAO.findByName(dto.getName())!=null)
            throw new ServiceException("name is reserved");

        //check mandatory and incompatible options logic
        checkCompatibility(dto);

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
        optionDAO.save(based);
        dto.setId(based.getId());
    }

    @Override
    public void update(TariffOptionDTO dto) throws ServiceException {
        TariffOption op = optionDAO.findByName(dto.getName());

        if (op != null && op.getId() != dto.getId())  //check if there is another option with the same name in database
            throw new ServiceException("name is reserved");

       //check mandatory and incompatible options compatibility
        checkCompatibility(dto);

        //update plain fields
        op=optionDAO.findOne(dto.getId());
        updatePlainFields(dto,op);

        //update complex fields
        op.getMandatoryOptions().clear();
        op.getIncompatibleOptions().clear();

        if (!dto.getIncompatible().isEmpty()){
            List<TariffOption> newIncompatibles=optionDAO.findByNames(dto.getIncompatible().toArray(new String[]{}));
            op.getIncompatibleOptions().addAll(newIncompatibles);
            for (TariffOption o:newIncompatibles) {
                o.addIncompatibleOption(op);
            }
        }
        if (!dto.getMandatory().isEmpty()){
            List<TariffOption> newMandatory=optionDAO.findByNames(dto.getMandatory().toArray(new String[]{}));
            op.getMandatoryOptions().addAll(newMandatory);
        }

        optionDAO.update(op);
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
    public TariffOptionTransfer getTransferForEdit(int id) {
        TariffOption option = optionDAO.findOne(id);
        TariffOptionDTO dto = new TariffOptionDTO(option);

        dto.setIncompatible(new HashSet<>(optionDAO.getAllIncompatibleNames(new String[]{option.getName()})));
        dto.setMandatory(new HashSet<>(optionDAO.getAllMandatoryNames(new String[]{option.getName()})));

//        dto.setIncompatible(option.getIncompatibleOptions().stream().map(TariffOption::getName).collect(Collectors.toSet())); //how to get only ids?
//        dto.setMandatory(option.getMandatoryOptions().stream().map(TariffOption::getName).collect(Collectors.toSet()));

        TariffOptionTransfer transfer = new TariffOptionTransfer(dto);
        List<String> map=optionDAO.getAllNames();
        map.remove(dto.getName());
        transfer.setAll(map);
        return transfer;
    }

    @Override
    public List<String> getAllNames() {
        return optionDAO.getAllNames();
    }

    @Override
    public TariffOption getFull(int id) {
        TariffOption tariffOption = optionDAO.findOne(id);
        Hibernate.initialize(tariffOption.getIncompatibleOptions());
        Hibernate.initialize(tariffOption.getMandatoryOptions());
        return tariffOption;
    }

    @Override
    public List<TariffOption> getAll() {
        return optionDAO.findAll();
    }

    private void checkCompatibility(TariffOptionDTO dto) throws ServiceException{
        //check if no option at the same time are mandatory and incompatible
        Optional<String> any = dto.getIncompatible().stream().filter(name -> dto.getMandatory().contains(name)).findFirst();
        if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE);

        //check if all from mandatory also have its corresponding mandatory options
        if (!dto.getMandatory().isEmpty())
        {
            String[] params=dto.getMandatory().toArray(new String[]{});
            List<String> names=optionDAO.getAllMandatoryNames(params);
            if (!names.isEmpty() && !dto.getMandatory().containsAll(names)) throw new ServiceException("More options are required as mandatory: "+names.toString());

        //check if mandatory options are incompatible with each other

            names=optionDAO.getAllIncompatibleNames(params);
        if (!names.isEmpty()) throw new ServiceException("Mandatory options are incompatible with each other");
        }

    }

    private void updatePlainFields(TariffOptionDTO dto,TariffOption based){
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setSubscribeCost(dto.getSubscribeCost());
        based.setDescription(dto.getDescription());
    }
}
