package services.implementations;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
import entities.dto.TariffDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.interfaces.TariffDaoI;
import repositories.interfaces.TariffOptionDaoI;
import services.ServiceException;
import services.interfaces.TariffServiceI;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class TariffService implements TariffServiceI {
    private static final String NAME_ERROR_MESSAGE = "name is reserved";

    private TariffDaoI tariffDAO;
    private TariffOptionDaoI optionDAO;


    @Autowired
    public void setTariffDAO(TariffDaoI tariffDAO, TariffOptionDaoI optionDAO) {
        this.tariffDAO = tariffDAO;
        this.optionDAO = optionDAO;
        tariffDAO.setClass(Tariff.class);
        optionDAO.setClass(TariffOption.class);
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
            TariffOption option = optionDAO.findByName(name);
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
            TariffOption newOption = optionDAO.findByName(name);
            t.addOption(newOption);
        }
        tariffDAO.update(t);
    }

    private void checkCompatibility(TariffDTO dto) throws ServiceException {
        //check if all options also have its corresponding mandatory options
        if (!dto.getOptions().isEmpty())
        {
            String[] params=dto.getOptions().toArray(new String[]{});
            List<String> names=optionDAO.getAllMandatoryNames(params);
            if (!names.isEmpty() && !dto.getOptions().containsAll(names)) throw new ServiceException("More options are required as mandatory: "+names.toString());

//            //check if options are incompatible with each other
//            names=optionDAO.getAllIncompatibleNames(params);
//            if (!names.isEmpty()){
//                Optional<String> any=names.stream().filter(name->dto.getOptions().contains(name)).findFirst();
//                if (any.isPresent()) throw new ServiceException("Selected options are incompatible with each other");
//            }
        }
    }

    @Override
    public Tariff getFull(int id) {
        Tariff tariff = tariffDAO.findOne(id);
        Hibernate.initialize(tariff.getOptions());
        return tariff;
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
        dto.setOptions(tariff.getOptions().stream().map(TariffOption::getName).collect(Collectors.toSet())); //how to get only ids?
        TariffTransfer transfer = new TariffTransfer(dto);
        List<String> map = optionDAO.getAllNames(); //do not include archived optionsNames
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
