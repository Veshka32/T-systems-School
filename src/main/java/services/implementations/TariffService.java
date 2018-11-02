package services.implementations;

import entities.Option;
import entities.Tariff;
import entities.dto.TariffDTO;
import entities.dto.TariffTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.interfaces.OptionDaoI;
import repositories.interfaces.TariffDaoI;
import services.ServiceException;
import services.interfaces.TariffServiceI;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        tariffDAO.setClass(Tariff.class);
        optionDAO.setClass(Option.class);
    }

    public TariffDTO getDto(int id) {
        TariffDTO dto = new TariffDTO(tariffDAO.findOne(id));
        dto.setOptions(new HashSet<>(optionDAO.getOptionsInTariffNames(id)));
        return dto;
    }

    @Override
    public void create(TariffDTO dto) throws ServiceException {
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

        tariffDAO.save(based);
        dto.setId(based.getId());
    }

    @Override
    public void update(TariffDTO dto) throws ServiceException {
        Tariff t = tariffDAO.findByName(dto.getName());

        if (t != null && t.getId() != dto.getId())  //check if there is another tariff with the same name in database
            throw new ServiceException(NAME_ERROR_MESSAGE);

        //check mandatory and incompatible options logic
        //throws exception if something is wrong
        checkCompatibility(dto);

        //update plain fields
        t = tariffDAO.findOne(dto.getId());
        updatePlainFields(dto, t);

        //update complex fields
        t.getOptions().clear();
        for (String name : dto.getOptions()) {
            Option newOption = optionDAO.findByName(name);
            t.addOption(newOption);
        }
        tariffDAO.update(t);
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
    public TariffTransfer getTransferForEdit(int id) {
        Tariff tariff = tariffDAO.findOne(id);
        TariffDTO dto = new TariffDTO(tariff);
        dto.setOptions(tariff.getOptions().stream().map(Option::getName).collect(Collectors.toSet())); //how to get only ids?
        TariffTransfer transfer = new TariffTransfer(dto);
        List<String> map = optionDAO.getAllNames();
        transfer.setAll(map);
        return transfer;
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
