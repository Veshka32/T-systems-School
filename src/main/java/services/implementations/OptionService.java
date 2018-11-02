package services.implementations;

import entities.OptionRelation;
import entities.RELATION;
import entities.TariffOption;
import entities.TariffOptionTransfer;
import entities.dto.TariffOptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.interfaces.RelationDaoI;
import repositories.interfaces.TariffOptionDaoI;
import services.ServiceException;
import services.interfaces.OptionServiceI;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private TariffOptionDaoI optionDAO;
    private RelationDaoI relationDaoI;
    private static final String ERROR_MESSAGE = "Option must not be both mandatory and incompatible";
    private static final String NAME_ERROR_MESSAGE = "name is reserved";

    @Autowired
    public void setOptionDAO(TariffOptionDaoI optionDAO, RelationDaoI relation) {
        this.optionDAO = optionDAO;
        //optionDAO.setClass(TariffOption.class);
        this.relationDaoI = relation;
        //relationDaoI.setClass(OptionRelation.class);
    }

    @Override
    public TariffOption get(int id) {
        return optionDAO.findOne(id);
    }

    @Override
    public void create(TariffOptionDTO dto) throws ServiceException {
        if (optionDAO.findByName(dto.getName()) != null)
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        //throws exception if something is wrong
        checkCompatibility(dto);

        //set plain fields
        TariffOption based = new TariffOption();
        updatePlainFields(dto, based);

        //set collections fields
        updateComplexFields(dto, based);

        optionDAO.save(based);
        dto.setId(based.getId());
    }

    @Override
    public void update(TariffOptionDTO dto) throws ServiceException {
        TariffOption op = optionDAO.findByName(dto.getName());

        if (op != null && op.getId() != dto.getId())  //check if there is another option with the same name in database
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options compatibility
        checkCompatibility(dto);

        //update plain fields
        op = optionDAO.findOne(dto.getId());
        updatePlainFields(dto, op);

        //clear and set complex fields
        relationDaoI.deleteAllIncompatible(op.getId());
        relationDaoI.deleteAllMandatory(op.getId());
        updateComplexFields(dto, op);

        optionDAO.update(op);
    }

    @Override
    public void delete(int id) throws ServiceException {
        if (!optionDAO.notUsed(id))
            throw new ServiceException("Option is used in contracts/tariffs or is mandatory for another option");

        relationDaoI.deleteAllMandatory(id);
        relationDaoI.deleteAllIncompatible(id);

        optionDAO.deleteById(id);
    }

    @Override
    public TariffOptionTransfer getTransferForEdit(int id) {
        TariffOptionDTO dto = getFull(id);

        TariffOptionTransfer transfer = new TariffOptionTransfer(dto);
        List<String> map = optionDAO.getAllNames();
        map.remove(dto.getName());
        transfer.setAll(map);
        return transfer;
    }

    @Override
    public List<String> getAllNames() {
        return optionDAO.getAllNames();
    }

    @Override
    public TariffOptionDTO getFull(int id) {
        TariffOption option = optionDAO.findOne(id);
        TariffOptionDTO dto = new TariffOptionDTO(option);

        dto.setIncompatible(new HashSet<>(optionDAO.getAllIncompatibleNames(new String[]{option.getName()})));
        dto.setMandatory(new HashSet<>(optionDAO.getAllMandatoryNames(new String[]{option.getName()})));

        return dto;
    }

    @Override
    public List<TariffOption> getAll() {
        return optionDAO.findAll();
    }

    private void checkCompatibility(TariffOptionDTO dto) throws ServiceException {
        //check if no option at the same time are mandatory and incompatible
        Optional<String> any = dto.getIncompatible().stream().filter(name -> dto.getMandatory().contains(name)).findFirst();
        if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE);

        //check if all from mandatory also have its corresponding mandatory options
        if (!dto.getMandatory().isEmpty()) {
            String[] params = dto.getMandatory().toArray(new String[]{});
            List<String> names = optionDAO.getAllMandatoryNames(params);
            names.remove(dto.getName()); //prevent itself requiring
            if (!names.isEmpty() && !dto.getMandatory().containsAll(names))
                throw new ServiceException("More options are required as mandatory: " + names.toString());

            //check if mandatory options are incompatible with each other
            names = optionDAO.getAllIncompatibleNames(params);
            if (!names.isEmpty()) {
                any = names.stream().filter(name -> dto.getMandatory().contains(name)).findFirst();
                if (any.isPresent()) throw new ServiceException("Mandatory options are incompatible with each other");
            }
        }
    }

    private void updatePlainFields(TariffOptionDTO dto, TariffOption based) {
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setSubscribeCost(dto.getSubscribeCost());
        based.setDescription(dto.getDescription());
    }

    private void updateComplexFields(TariffOptionDTO dto, TariffOption op) {
        //set collections fields
        for (String name : dto.getIncompatible()) {
            TariffOption newIncompatible = optionDAO.findByName(name);
            OptionRelation r = new OptionRelation(op, newIncompatible, RELATION.INCOMPATIBLE);
            OptionRelation r1 = new OptionRelation(newIncompatible, op, RELATION.INCOMPATIBLE);
            relationDaoI.save(r);
            relationDaoI.save(r1);
        }

        for (String name : dto.getMandatory()) {
            TariffOption newMandatory = optionDAO.findByName(name);
            OptionRelation r = new OptionRelation(op, newMandatory, RELATION.MANDATORY);
            relationDaoI.save(r);
        }
    }
}
