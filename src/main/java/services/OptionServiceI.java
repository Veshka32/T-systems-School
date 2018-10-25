package services;

import entities.TariffOption;

import java.util.List;

public interface OptionServiceI {

        void save(TariffOption option);

        void delete(int id);

        TariffOption get(int id);

        TariffOption findByName(String name);

        List<TariffOption> getAll();
        
        void update(TariffOption option);
        void updatePrice(int id,int price);

        void updateSubscribeCost(int id,int cost);

        void removeIncompatibleOption(int id, int optionId);

        void archive(int id);

}
