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
import services.exceptions.ServiceException;
import services.interfaces.TariffServiceI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public TariffDTO getDto(int id) {
        Tariff tariff = tariffDAO.findOne(id);
        TariffDTO dto = new TariffDTO(tariff);
        dto.getOptions().addAll(tariff.getOptions().stream().map(Option::getId).collect(Collectors.toList()));
        dto.setOptionsNames(tariff.getOptions().stream().map(Option::getName).collect(Collectors.joining(", ")));
        return dto;
    }

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

    @Override
    public List<Tariff> getLast(int count) {
        return tariffDAO.getLast(count);
    }

    @Override
    public int create(TariffDTO dto) throws ServiceException {
        if (tariffDAO.findByName(dto.getName()) != null)
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        //throws exception if something is wrong
        checkCompatibility(dto);

        //set plain fields
        Tariff based = new Tariff();
        updatePlainFields(dto, based);

        //set complex fields
        for (Integer id : dto.getOptions()) {
            Option option = optionDAO.findOne(id);
            based.addOption(option);
        }
        return tariffDAO.save(based);
    }

    @Override
    public void update(TariffDTO dto) throws ServiceException {
        Tariff tariff = tariffDAO.findByName(dto.getName());

        //check if there is another tariff with the same name in database
        if (tariff != null && tariff.getId() != dto.getId())
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic, throws exception if something is wrong
        checkCompatibility(dto);

        //update plain fields
        tariff = tariffDAO.findOne(dto.getId());
        updatePlainFields(dto, tariff);

        //update complex fields
        tariff.getOptions().clear();
        for (Integer id : dto.getOptions()) {
            Option newOption = optionDAO.findOne(id);
            tariff.addOption(newOption);
        }
        tariffDAO.update(tariff);
    }

    private void checkCompatibility(TariffDTO dto) throws ServiceException {
        if (dto.getOptions().isEmpty()) return;

        //check if all from mandatory also have its' corresponding mandatory options
        Integer[] ids = dto.getOptions().toArray(new Integer[]{});
        List<OptionRelation> relation = optionDAO.getMandatoryRelation(ids);

        if (!relation.isEmpty()) {
            Set<String> required = relation.stream().filter(r -> !dto.getOptions().contains(r.getAnother().getId())).map(r -> r.getAnother().getName()).collect(Collectors.toSet());
            if (!required.isEmpty()) {
                throw new ServiceException("More options are required as mandatory: " + required.stream().collect(Collectors.joining(", ")));
            }
        }

        //check if mandatory options are incompatible with each other
        relation = optionDAO.getIncompatibleRelationInRange(ids);
        if (!relation.isEmpty()) {
            String s = relation.stream().map(r -> r.getOne().getName() + " and " + r.getAnother().getName()).collect(Collectors.joining(", "));
            throw new ServiceException("Options " + s + " incompatible with each other");
        }
    }

    private void updatePlainFields(TariffDTO dto, Tariff based) {
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setDescription(dto.getDescription());
    }

    @Override
    public void delete(int id) throws ServiceException {
        if (tariffDAO.isUsed(id)) throw new ServiceException("tariff is used in some contracts");
        tariffDAO.deleteById(id);
    }

    @Override
    public List<Tariff> getAll() {
        return tariffDAO.findAll();
    }


    @Override
    public Map<String, Integer> getAllNamesWithIds() {
        List<Object[]> all = tariffDAO.getAllNamesAndIds();
        return all.stream().collect(HashMap::new, (m, array) -> m.put((String) array[1], (Integer) array[0]), Map::putAll);
    }

}
