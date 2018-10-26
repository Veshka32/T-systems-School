package services;

import entities.TariffOption;
import entities.TariffOptionTransfer;

import java.util.List;

public interface OptionServiceI {


    void create(TariffOption option) throws OptionException;

    void save(TariffOption option);

    void delete(int id);

    TariffOption get(int id);

    TariffOptionTransfer getTransfer(int id);

    TariffOption getFull(int id);

    TariffOption findByName(String name);

    List<TariffOption> getAll();

    List<String> getAllNames();

    void update(TariffOption option) throws OptionException;

    void removeIncompatibleOption(int id, int optionId);

    void addIncompatibleOption(int id, String optionName) throws OptionException;

    void removeMandatoryOption(int id, int optionId);

    void addMandatoryOption(int id, String optionName) throws OptionException;

}
