package services.implementations;

import dao.interfaces.OptionDaoI;
import dao.interfaces.RelationDaoI;
import model.dto.OptionDTO;
import model.entity.Option;
import model.entity.OptionRelation;
import model.enums.RELATION;
import model.helpers.PaginateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.ServiceException;
import services.interfaces.OptionServiceI;

import java.util.List;
import java.util.Optional;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private OptionDaoI optionDAO;
    private RelationDaoI relationDaoI;
    private static final String ERROR_MESSAGE = "Option must not be both mandatory and incompatible";
    private static final String NAME_ERROR_MESSAGE = "name is reserved";

    @Autowired
    public void setOptionDAO(OptionDaoI optionDAO, RelationDaoI relation) {
        this.optionDAO = optionDAO;
        this.optionDAO.setClass(Option.class);
        this.relationDaoI = relation;
        this.relationDaoI.setClass(OptionRelation.class);
    }

    @Override
    public Option get(int id) {
        return optionDAO.findOne(id);
    }

    @Override
    public int create(OptionDTO dto) throws ServiceException {
        //check name uniqueness
        if (optionDAO.findByName(dto.getName()) != null)
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic, throws exception if something is wrong
        checkCompatibility(dto);

        //set plain fields
        Option based = new Option();
        updatePlainFields(dto, based);

        optionDAO.save(based);

        //set collections fields
        updateCollectionFields(dto, based);

        return based.getId();
    }

    @Override
    public void update(OptionDTO dto) throws ServiceException {
        Option option = optionDAO.findByName(dto.getName());

        //check if there is another option with the same name in database and it is not proceeded option
        if (option != null && option.getId() != dto.getId())
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic, throws exception if something is wrong
        checkCompatibility(dto);

        //update plain fields
        option = optionDAO.findOne(dto.getId());
        updatePlainFields(dto, option);

        //clear and set complex fields
        relationDaoI.deleteAllIncompatible(option.getId());
        relationDaoI.deleteAllMandatory(option.getId());
        updateCollectionFields(dto, option);

        //save changes in db
        optionDAO.update(option);
    }

    @Override
    public void delete(int id) throws ServiceException {
        //check if option has any relation with other options, tariffs or contract
        if (!optionDAO.notUsed(id))
            throw new ServiceException("Option is used in contracts/tariffs or is mandatory for another option");

        relationDaoI.deleteAllMandatory(id);
        relationDaoI.deleteAllIncompatible(id);
        optionDAO.deleteById(id);
    }

    @Override
    public List<String> getAllNames() {
        return optionDAO.getAllNames();
    }

    @Override
    public OptionDTO getDto(int id) {
        Option option = optionDAO.findOne(id);
        OptionDTO dto = new OptionDTO(option);
        dto.getIncompatible().addAll(optionDAO.getAllIncompatibleNames(new String[]{option.getName()}));
        dto.getMandatory().addAll(optionDAO.getAllMandatoryNames(new String[]{option.getName()}));
        return dto;
    }

    @Override
    public List<Option> getAll() {
        return optionDAO.findAll();
    }

    private void checkCompatibility(OptionDTO dto) throws ServiceException {
        dto.getMandatory().remove(dto.getName());
        dto.getIncompatible().remove(dto.getName());

        //check if no option at the same time are mandatory and incompatible
        Optional<String> any = dto.getIncompatible().stream().filter(name -> dto.getMandatory().contains(name)).findFirst();
        if (any.isPresent()) throw new ServiceException(ERROR_MESSAGE + " :" + any.get());

        //check if all from mandatory also have its' corresponding mandatory options
        if (!dto.getMandatory().isEmpty()) {
            String[] params = dto.getMandatory().toArray(new String[]{});
            List<String> names = optionDAO.getAllMandatoryNames(params);

            //check if mandatory relation is not bidirectional
            if (names.contains(dto.getName())) {
                List<String> names1 = optionDAO.getMandatoryFor(dto.getId());
                throw new ServiceException("Option " + dto.getName() + " is already mandatory itself for these options: " + names1.toString());
            }

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

    @Override
    public PaginateHelper<Option> getPaginateData(Integer currentPage, int rowPerPage) {
        if (currentPage == null) currentPage = 1;  //if no page specified, show first page
        if (currentPage < 1 || rowPerPage < 0) throw new IllegalArgumentException();
        List<Option> optionsForPage = optionDAO.allInRange((currentPage - 1) * rowPerPage, rowPerPage);
        int total = optionDAO.count().intValue();
        int totalPage = total / rowPerPage;
        if (total % rowPerPage > 0) totalPage++;
        return new PaginateHelper<>(optionsForPage, totalPage);
    }

    private void updatePlainFields(OptionDTO dto, Option based) {
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setSubscribeCost(dto.getSubscribeCost());
        based.setDescription(dto.getDescription());
    }

    private void updateCollectionFields(OptionDTO dto, Option op) {
        //set collections fields
        for (String name : dto.getIncompatible()) {
            Option newIncompatible = optionDAO.findByName(name);
            OptionRelation r = new OptionRelation(op, newIncompatible, RELATION.INCOMPATIBLE);
            relationDaoI.save(r);
        }

        for (String name : dto.getMandatory()) {
            Option newMandatory = optionDAO.findByName(name);
            OptionRelation r = new OptionRelation(op, newMandatory, RELATION.MANDATORY);
            relationDaoI.save(r);
        }
    }
}
