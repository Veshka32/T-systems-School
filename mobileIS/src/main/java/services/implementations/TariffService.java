package services.implementations;

import dao.interfaces.OptionDaoI;
import dao.interfaces.TariffDaoI;
import model.dto.TariffDTO;
import model.entity.Option;
import model.entity.OptionRelation;
import model.entity.Tariff;
import model.helpers.PaginateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.TariffServiceI;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class TariffService implements TariffServiceI {
    private static final String NAME_ERROR_MESSAGE = "name is reserved";

    private TariffDaoI tariffDAO;
    private OptionDaoI optionDAO;

    @Autowired
    public void setTariffDAO(TariffDaoI tariffDAO, OptionDaoI optionDAO) {
        this.tariffDAO = tariffDAO;
        this.optionDAO = optionDAO;
        this.tariffDAO.setClass(Tariff.class);
        this.optionDAO.setClass(Option.class);
    }

    /**
     * Build data transfer object for {@code Contract} with specific id, including initialized contained options
     *
     * @param id database id of desired {@code Contract} object
     * @return {@code ContractDTO} object contains contract properties
     */
    public TariffDTO getDto(int id) {
        Tariff tariff = tariffDAO.findOne(id);
        TariffDTO dto = new TariffDTO(tariff);
        dto.getOptions().addAll(tariff.getOptions().stream().map(Option::getId).collect(Collectors.toList()));
        dto.setOptionsNames(tariff.getOptions().stream().map(Option::getName).collect(Collectors.joining(", ")));
        return dto;
    }

    /**
     * Construct object contains tariffs in specific range from database and additional info (total number of tariffs in database)
     *
     * @param currentPage current page on view to build
     * @param rowPerPage  number of items for one page (equals total number of items in {@code PaginateHelper}
     * @return {@code PaginateHelper} with tariffs in specific range and additional info
     */
    @Override
    public PaginateHelper<Tariff> getPaginateData(Integer currentPage, int rowPerPage) {
        if (currentPage == null) currentPage = 1;  //if no page specified, return first page
        if (currentPage < 1 || rowPerPage < 0) throw new IllegalArgumentException();

        List<Tariff> tariffsForPage = tariffDAO.allInRange((currentPage - 1) * rowPerPage, rowPerPage);
        int total = tariffDAO.count().intValue();
        int totalPage = total / rowPerPage;
        if (total % rowPerPage > 0) totalPage++;
        return new PaginateHelper<>(tariffsForPage, totalPage);
    }

    /**
     * Retrieve last n tariffs from database ordered by id
     *
     * @param count desired number of tariffs
     * @return list with tariffs of specific size (or less if there are no so many in database)
     */
    @Override
    public List<Tariff> getLast(int count) {
        return tariffDAO.getLast(count);
    }

    /**
     * Create new {@code Tariff} based on dto properties
     *
     * @param dto data transfer object contains required properties
     * @return empty Optional if tariff is successfully created or error message if not
     */
    @Override
    public Optional<String> create(TariffDTO dto) {
        if (tariffDAO.findByName(dto.getName()) != null)
            return Optional.of(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        Optional<String> error = checkCompatibility(dto);
        if (error.isPresent()) return error;

        //set plain fields
        Tariff tariff = new Tariff();
        updatePlainFields(dto, tariff);

        //set complex fields
        updateComplexFields(dto, tariff);
        dto.setId(tariffDAO.save(tariff));
        return Optional.empty();
    }


    /**
     * Update all fields in corresponding {@code Tariff}  with values from data transfer object
     *
     * @param dto data transfer object contains option id and properties
     * @return empty Optional if tariff is successfully updated or error message if not
     */
    @Override
    public Optional<String> update(TariffDTO dto) {
        Tariff tariff = tariffDAO.findByName(dto.getName());

        //check if there is another tariff with the same name in database
        if (tariff != null && tariff.getId() != dto.getId())
            return Optional.of(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        Optional<String> error = checkCompatibility(dto);
        if (error.isPresent()) return error;

        //update plain fields
        tariff = tariffDAO.findOne(dto.getId());
        updatePlainFields(dto, tariff);

        //update complex fields
        tariff.getOptions().clear();
        updateComplexFields(dto, tariff);
        tariffDAO.update(tariff);
        return Optional.empty();
    }


    /**
     * Delete {@code Option} with specific id from database if it is not used in any tariff, contract or if it is not mandatory for any option.
     *
     * @param id id of client to delete
     * @return empty Optional if option is successfully deleted or error message if not
     */
    @Override
    public Optional<String> delete(int id) {
        if (tariffDAO.isUsed(id)) return Optional.of("tariff is used in some contracts");
        tariffDAO.deleteById(id);
        return Optional.empty();
    }

    /*
     * Check compatibility of options included in target tariff.
     * Return either Optional with error message or empty Optional if logic is ok
     */
    private Optional<String> checkCompatibility(TariffDTO dto) {
        //nothing to check
        if (dto.getOptions().isEmpty()) return Optional.empty();

        //check if all from mandatory also have its' corresponding mandatory options
        Integer[] ids = dto.getOptions().toArray(new Integer[]{});
        List<OptionRelation> relation = optionDAO.getMandatoryRelation(ids);

        if (!relation.isEmpty()) {
            Set<String> required = relation.stream().filter(r -> !dto.getOptions().contains(r.getAnother().getId())).map(r -> r.getAnother().getName()).collect(Collectors.toSet());
            if (!required.isEmpty()) {
                return Optional.of("More options are required as mandatory: " + required.stream().collect(Collectors.joining(", ")));
            }
        }

        //check if mandatory options are incompatible with each other
        relation = optionDAO.getIncompatibleRelationInRange(ids);
        if (!relation.isEmpty()) {
            String s = relation.stream().map(r -> r.getOne().getName() + " and " + r.getAnother().getName()).collect(Collectors.joining(", "));
            return Optional.of("Options " + s + " incompatible with each other");
        }

        return Optional.empty();
    }

    private void updatePlainFields(TariffDTO dto, Tariff based) {
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setDescription(dto.getDescription());
    }

    private void updateComplexFields(TariffDTO dto, Tariff tariff) {
        for (Integer id : dto.getOptions()) {
            Option option = optionDAO.findOne(id);
            tariff.addOption(option);
        }
    }
}
