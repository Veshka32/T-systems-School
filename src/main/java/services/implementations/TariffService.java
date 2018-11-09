package services.implementations;

import dao.interfaces.OptionDaoI;
import dao.interfaces.TariffDaoI;
import entities.Option;
import entities.Tariff;
import entities.dto.TariffDTO;
import entities.helpers.PaginateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import services.ServiceException;
import services.interfaces.TariffServiceI;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        TariffDTO dto = new TariffDTO(tariffDAO.findOne(id));
        dto.setOptions(new HashSet<>(optionDAO.getOptionsInTariffNames(id)));
        return dto;
    }

    @Override
    public PaginateHelper<Tariff> getPaginateData(Integer currentPage, int rowPerPage) {
        if (currentPage == null) currentPage = 1;  //if no page specified, return first page
        List<Tariff> tariffsForPage = tariffDAO.allInRange((currentPage - 1) * rowPerPage, rowPerPage);
        int total = tariffDAO.count().intValue();
        int totalPage = total / rowPerPage;
        if (total % rowPerPage > 0) totalPage++;
        return new PaginateHelper<>(tariffsForPage, totalPage);
    }

    @Override
    public int create(TariffDTO dto) throws ServiceException {
        if (tariffDAO.isNameExist(dto.getName()))
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        //throws exception if something is wrong
        checkCompatibility(dto);

        //set plain fields
        Tariff based = new Tariff();
        updatePlainFields(dto, based);

        //set complex fields
        for (String name : dto.getOptions()) {
            Option option = optionDAO.findByName(name);
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

        //check mandatory and incompatible options logic
        //throws exception if something is wrong
        checkCompatibility(dto);

        //update plain fields
        tariff = tariffDAO.findOne(dto.getId());
        updatePlainFields(dto, tariff);

        //update complex fields
        tariff.getOptions().clear();
        for (String name : dto.getOptions()) {
            Option newOption = optionDAO.findByName(name);
            tariff.addOption(newOption);
        }
        tariffDAO.update(tariff);
    }

    private void checkCompatibility(TariffDTO dto) throws ServiceException {
        //check if all options also have its' corresponding mandatory options
        if (!dto.getOptions().isEmpty())
        {
            String[] params=dto.getOptions().toArray(new String[]{});
            List<String> names=optionDAO.getAllMandatoryNames(params);
            if (!names.isEmpty() && !dto.getOptions().containsAll(names))
                throw new ServiceException("More options are required as mandatory: " + names.toString());

            //check if options are incompatible with each other
            names = optionDAO.getAllIncompatibleNames(params);
            if (!names.isEmpty()) {
                Optional<String> any = names.stream().filter(name -> dto.getOptions().contains(name)).findFirst();
                if (any.isPresent())
                    throw new ServiceException("Selected options are incompatible with each other");
            }
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
    public Tariff get(int id) {
        return tariffDAO.findOne(id);
    }

    @Override
    public List<Tariff> getAll() {
        return tariffDAO.findAll();
    }

    @Override
    public List<String> getAllNames() {
        return tariffDAO.getAllNames();
    }

}
