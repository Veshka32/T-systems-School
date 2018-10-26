package services;

import entities.TariffOption;

import java.util.List;
import java.util.Set;

public interface OptionServiceI {


        void create(TariffOption option) throws OptionException;
        void save(TariffOption option);

        void delete(int id);

        TariffOption get(int id);

        TariffOption getFull(int id);

        TariffOption findByName(String name);

        List<TariffOption> getAll();
        List<String> getAllNames();

        public Set<TariffOption> getIncompatible(int id);
        
        void update(TariffOption option) throws OptionException;
        void updatePrice(int id,int price);

        void updateSubscribeCost(int id,int cost);

        void removeIncompatibleOption(int id, int optionId);
        void addIncompatibleOption(int id, String optionName) throws OptionException;
    void removeMandatoryOption(int id, int optionId);
    void addMandatoryOption(int id, String optionName) throws OptionException;

    void archive(int id);

}
