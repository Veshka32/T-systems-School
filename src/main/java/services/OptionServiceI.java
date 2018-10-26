package services;

import entities.TariffOption;

import java.util.List;
import java.util.Set;

public interface OptionServiceI {

        void save(TariffOption option);

        void delete(int id);

        TariffOption get(int id);

        TariffOption getFull(int id);

        TariffOption findByName(String name);

        List<TariffOption> getAll();

        public Set<TariffOption> getIncompatible(int id);
        
        void update(TariffOption option);
        void updatePrice(int id,int price);

        void updateSubscribeCost(int id,int cost);

        void removeIncompatibleOption(int id, int optionId);
        void addIncompatibleOption(int id, int optionId);

        void archive(int id);

}
