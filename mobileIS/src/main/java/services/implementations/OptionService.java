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
import services.exceptions.ServiceException;
import services.interfaces.OptionServiceI;

import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class OptionService implements OptionServiceI {

    private OptionDaoI optionDAO;
    private RelationDaoI relationDaoI;
    private static final String ERROR_MESSAGE = "Option must not be both mandatory and incompatible: ";
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
    public Map<String, Integer> getAllNamesWithIds() {

        return optionDAO.getAllNamesAndIds().stream().collect(HashMap::new, (m, array) -> m.put((String) array[1], (Integer) array[0]), Map::putAll);
    }

    @Override
    public OptionDTO getDto(int id) {
        Option option = optionDAO.findOne(id);
        OptionDTO dto = new OptionDTO(option);
        Integer[] ids = {dto.getId()};
        List<OptionRelation> mandatory = optionDAO.getMandatoryRelation(ids);
        List<OptionRelation> incompatible = optionDAO.getIncompatibleRelation(ids);

        dto.setMandatoryNames(mandatory.stream().map(r -> r.getAnother().getName()).collect(Collectors.joining(", ")));
        dto.setIncompatibleNames(incompatible.stream().map(r -> (r.getAnother().getId() == id ? r.getOne().getName() : r.getAnother().getName())).collect(Collectors.joining(", ")));
        dto.getMandatory().addAll(mandatory.stream().map(r -> r.getAnother().getId()).collect(Collectors.toList()));
        dto.getIncompatible().addAll(incompatible.stream().map(r -> (r.getAnother().getId() == id ? r.getOne().getId() : r.getAnother().getId())).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public List<Option> getAll() {
        return optionDAO.findAll();
    }

    private void checkCompatibility(OptionDTO dto) throws ServiceException {
        dto.getMandatory().remove(dto.getId());
        dto.getIncompatible().remove(dto.getId());

        //check if no option at the same time are mandatory and incompatible
        Optional<Integer> any = dto.getIncompatible().stream().filter(id -> dto.getMandatory().contains(id)).findFirst();
        if (any.isPresent())
            throw new ServiceException(ERROR_MESSAGE + optionDAO.findOne(any.get()).getName());
        if (dto.getMandatory().isEmpty()) return;

        //check if all from mandatory also have its' corresponding mandatory options
        Integer[] mandatoryIds = dto.getMandatory().toArray(new Integer[]{});
        List<OptionRelation> relations = optionDAO.getMandatoryRelation(mandatoryIds);

        if (!relations.isEmpty()) {
            //check if mandatory relation is not bidirectional
            List<OptionRelation> selfPointer = relations.stream().
                    filter(r -> r.getAnother().getId() == dto.getId())
                    .collect(Collectors.toList());
            if (!selfPointer.isEmpty())
                throw new ServiceException(
                        "Option " + dto.getName() + " is already mandatory itself for these options: " +
                                selfPointer.stream().map(r -> r.getOne().getName()).collect(Collectors.joining(", ")));

            Set<OptionRelation> allMandatories = relations.stream()
                    .filter(r -> !dto.getMandatory().contains(r.getAnother().getId()))
                    .collect(Collectors.toSet());
            if (!allMandatories.isEmpty()) {
                throw new ServiceException("More options are required as mandatory: " +
                        allMandatories.stream().map(r -> r.getAnother().getName()).collect(Collectors.joining(", ")));
            }
        }

        //check if mandatory options are incompatible with each other
        List<OptionRelation> relations1 = optionDAO.getIncompatibleRelation(mandatoryIds);
        if (!relations1.isEmpty()) {
            String s = relations1.stream().map(r -> r.getOne().getName() + " and " + r.getAnother().getName()).collect(Collectors.joining(", "));
            throw new ServiceException("Options " + s + " incompatible with each other");
        }
    }

    @Override
    public PaginateHelper<Option> getPaginateData(Integer currentPage, int rowPerPage) {
        if (currentPage == null) currentPage = 1;  //if no page specified, show first page
        if (currentPage < 1 || rowPerPage < 0) throw new IllegalArgumentException();
        int total = optionDAO.count().intValue();
        List<Option> optionsForPage = optionDAO.allInRange((currentPage - 1) * rowPerPage, rowPerPage);
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
        for (int id : dto.getIncompatible()) {
            Option newIncompatible = optionDAO.findOne(id);
            OptionRelation r = new OptionRelation(op, newIncompatible, RELATION.INCOMPATIBLE);
            relationDaoI.save(r);
        }

        for (int id : dto.getMandatory()) {
            Option newMandatory = optionDAO.findOne(id);
            OptionRelation r = new OptionRelation(op, newMandatory, RELATION.MANDATORY);
            relationDaoI.save(r);
        }
    }
}
