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
import services.aspects.Loggable;
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
    @Loggable
    public Optional<String> create(OptionDTO dto) {
        //check name uniqueness

        if (optionDAO.findByName(dto.getName()).isPresent())
            return Optional.of(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        Optional<String> error = checkCompatibility(dto);
        if (error.isPresent()) return error;

        //set plain fields
        Option based = new Option();
        updatePlainFields(dto, based);

        dto.setId(optionDAO.save(based));

        //set collections fields
        updateCollectionFields(dto, based);
        return Optional.empty();
    }

    @Override
    @Loggable
    public Optional<String> update(OptionDTO dto) {
        Optional<Option> optional = optionDAO.findByName(dto.getName());

        //check if there is another option with the same name in database and it is not proceeded option
        if (optional.isPresent() && optional.get().getId() != dto.getId())
            return Optional.of(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        Optional<String> error = checkCompatibility(dto);
        if (error.isPresent()) return error;

        //update plain fields
        Option option = optionDAO.findOne(dto.getId());
        updatePlainFields(dto, option);

        //clear and set complex fields
        relationDaoI.deleteAllIncompatible(option.getId());
        relationDaoI.deleteAllMandatory(option.getId());
        updateCollectionFields(dto, option);

        //save changes in db
        optionDAO.update(option);
        return Optional.empty();
    }

    @Override
    @Loggable
    public Optional<String> delete(int id) {
        //check if option has any relation with other options, tariffs or contract
        if (!optionDAO.notUsed(id))
            return Optional.of("Option is used in contracts/tariffs or is mandatory for another option");
        relationDaoI.deleteAllMandatory(id);
        relationDaoI.deleteAllIncompatible(id);
        optionDAO.deleteById(id);
        return Optional.empty();
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
        List<OptionRelation> incompatible = optionDAO.getIncompatibleRelationInRange(ids);

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

    private Optional<String> checkCompatibility(OptionDTO dto) {
        dto.getMandatory().remove(dto.getId());
        dto.getIncompatible().remove(dto.getId());

        //check if no option at the same time are mandatory and incompatible
        Optional<Integer> any = dto.getIncompatible().stream().filter(id -> dto.getMandatory().contains(id)).findFirst();
        if (any.isPresent()) return Optional.of(ERROR_MESSAGE + optionDAO.findOne(any.get()).getName());

        //nothing to check
        if (dto.getMandatory().isEmpty()) return Optional.empty();

        Integer[] ids = dto.getMandatory().toArray(new Integer[]{});
        List<OptionRelation> relations = optionDAO.getMandatoryRelation(ids);

        if (!relations.isEmpty()) {
            //check if mandatory relation is not bidirectional
            List<OptionRelation> selfPointer = relations.stream()
                    .filter(r -> r.getAnother().getId() == dto.getId())
                    .collect(Collectors.toList());
            if (!selfPointer.isEmpty())
                return Optional.of("Option " + dto.getName() + " is already mandatory itself for these options: " + selfPointer.stream().map(r -> r.getOne().getName()).collect(Collectors.joining(", ")));

            //check if all from mandatory set also have its' corresponding mandatory options
            Set<String> required = relations.stream()
                    .filter(r -> !dto.getMandatory().contains(r.getAnother().getId()))
                    .map(r -> r.getAnother().getName()).collect(Collectors.toSet());
            if (!required.isEmpty()) {
                return Optional.of("More options are required as mandatory: " + required.stream().collect(Collectors.joining(", ")));
            }
        }

        //check if mandatory options are incompatible with each other
        relations = optionDAO.getIncompatibleRelationInRange(ids);
        if (!relations.isEmpty()) {
            String s = relations.stream().map(r -> r.getOne().getName() + " and " + r.getAnother().getName()).collect(Collectors.joining(", "));
            return Optional.of("Options " + s + " incompatible with each other");
        }
        return Optional.empty();
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
