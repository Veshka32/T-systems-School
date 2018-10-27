package services;

import entities.Tariff;
import entities.TariffOption;
import entities.TariffTransfer;
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
    public int create(Tariff tariff) throws ServiceException {
        if (tariffDAO.isNameExist(tariff.getName()))
            throw new ServiceException("name is reserved");

        Set<TariffOption> tariffOptions = tariff.getOptions();

        for (TariffOption option : tariffOptions) {

            List<TariffOption> bad =optionDAO.getIncompatibleOptions(option.getId());                    ;
            Optional<TariffOption> badOption = bad.stream().filter(x -> tariffOptions.contains(x)).findFirst();
            if (badOption.isPresent()) {
                throw new ServiceException("Options are incompatible: " + option.getName() + " and " + badOption.get().getName());
            }
        }

        for (TariffOption option : tariffOptions) {
            List<TariffOption> mandatory = optionDAO.getMandatoryOptions(option.getId());
            if (!mandatory.isEmpty() && !tariffOptions.contains(mandatory)) {
                throw new ServiceException("Can't set option: " + option.getName() + " without options: " + mandatory.stream().map(TariffOption::getName).collect(Collectors.toList()).toString());
            }
        }
        return tariffDAO.save(tariff);
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
    public void addOption(int tariffId, int optionId) throws ServiceException {
        Tariff tariff = tariffDAO.findOne(tariffId);
        Hibernate.initialize(tariff.getOptions());
        TariffOption option = optionDAO.findOne(optionId);
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
