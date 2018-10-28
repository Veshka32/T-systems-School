package services;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
import entities.dto.TariffDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import repositories.TariffDAO;
import repositories.TariffOptionDAO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
@Transactional
public class TariffService implements TariffServiceI {

    private TariffDAO tariffDAO;
    private TariffOptionDAO optionDAO;

    @Autowired
    public void setTariffDAO(TariffDAO tariffDAO, TariffOptionDAO optionDAO) {
        this.tariffDAO = tariffDAO;
        this.optionDAO = optionDAO;
        tariffDAO.setClass(Tariff.class);
        optionDAO.setClass(TariffOption.class);
    }

    @Override
    public void create(TariffDTO dto) throws ServiceException {
        if (tariffDAO.isNameExist(dto.getName()))
            throw new ServiceException("name is reserved");

        //check if options are compatible with each other
        //check if each option has corresponding mandatory
        for (String name:dto.getOptions()) {
            TariffOption option=optionDAO.findByName(name);
            Optional<String> any=option.getIncompatibleOptions().stream().map(o->o.getName()).filter(o->dto.getOptions().contains(o)).findAny();
            if (any.isPresent()) throw new ServiceException("You choose incompatible options: "+name+", "+any.get());
            any=option.getMandatoryOptions().stream().map(TariffOption::getName).filter(o->!dto.getOptions().contains(o)).findAny();
            if (any.isPresent()) throw new ServiceException("Option "+name+" requires option "+any.get());
        }
        //set plain fields
        Tariff based = new Tariff();
        updatePlainFields(dto,based);

        //set complex fields
        for (String name:dto.getOptions()) {
            TariffOption option=optionDAO.findByName(name);
            based.addOption(option);
        }
        tariffDAO.save(based);
        dto.setId(based.getId());
    }

    @Override
    public Tariff getFull(int id) {
        Tariff tariff = tariffDAO.findOne(id);
        Hibernate.initialize(tariff.getOptions());
        return tariff;
    }

    private void updatePlainFields(TariffDTO dto,Tariff based){
        based.setName(dto.getName());
        based.setPrice(dto.getPrice());
        based.setArchived(dto.isArchived());
        based.setDescription(dto.getDescription());
    }

    @Override
    public TariffTransfer getTransfer(int id) {
        Tariff tariff = tariffDAO.findOne(id);
        Hibernate.initialize(tariff.getOptions());
        TariffTransfer transfer = new TariffTransfer(tariff);
        transfer.setAll(optionDAO.getAllNames());
        return transfer;
    }

    @Override
    public void delete(int id) throws ServiceException {
        if (tariffDAO.isUsed(id)) throw new ServiceException("tariff is used in some contracts");
        tariffDAO.deleteById(id);
    }

    @Override
    public void update(Tariff tariff) throws ServiceException {
        Tariff t = tariffDAO.findByName(tariff.getName());

        if (t != null && t.getId() != tariff.getId())  //check if there is another option with the same name in database
            throw new ServiceException("name is reserved");

        tariffDAO.update(tariff);
    }

    @Override
    public Tariff get(int id) {
        return tariffDAO.findOne(id);
    }

    @Override
    public Tariff findByName(String name) {
        /**
         * TODO
         */
        return null;
    }

    @Override
    public List<Tariff> getAll() {
        return tariffDAO.findAll();
    }

    @Override
    public void deleteOption(int tariffId, int optionId) throws ServiceException {
        Tariff tariff = tariffDAO.findOne(tariffId);
        TariffOption option = optionDAO.findOne(optionId);
        List<TariffOption> mandatory = tariff.getOptions().stream().filter(o -> o.getMandatoryOptions().contains(option)).collect(Collectors.toList());
        if (!mandatory.isEmpty())
            throw new ServiceException("Options " + mandatory.stream().map(TariffOption::getName).collect(Collectors.joining(",")) + "require option " + option.getName());
        tariff.deleteOption(optionDAO.findOne(optionId));
        tariffDAO.update(tariff);
    }

    @Override
    public void addOption(int tariffId, String optionName) throws ServiceException {
        Tariff tariff = tariffDAO.findOne(tariffId);
        Hibernate.initialize(tariff.getOptions());
        TariffOption option = optionDAO.findByName(optionName);
        Optional<TariffOption> bad = tariff.getOptions().stream().flatMap(x -> x.getIncompatibleOptions().stream()).filter(x -> x.equals(option)).findFirst();
        if (bad.isPresent())
            throw new ServiceException("Option " + option.getName() + " incompatible with " + bad.get().getName());

        List<TariffOption> missedMandatory = option.getMandatoryOptions().stream().filter(o -> !tariff.getOptions().contains(o)).collect(Collectors.toList());
        if (!missedMandatory.isEmpty())
            throw new ServiceException("Option " + option.getName() + " requires these options: " + missedMandatory.stream().map(TariffOption::getName).collect(Collectors.joining(",")));
        tariff.addOption(option);
        tariffDAO.update(tariff);
    }

    @Override
    public List<TariffOption> getTariffOptions(int id) {
        return tariffDAO.getOptions(id);
    }

    @Override
    public List<TariffOption> getAvailableOptions(int id) {
        List<TariffOption> tariffOptions = getTariffOptions(id);
        return optionDAO.findAll().stream()
                .filter(o -> !tariffOptions.contains(o)).collect(Collectors.toList());
    }
}
