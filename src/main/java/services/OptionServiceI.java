package services;

import entities.Tariff;
import entities.TariffOption;

import java.util.List;

public interface OptionServiceI {

        void create(String name,int price);

        void delete(int id);

        TariffOption get(int id);

        TariffOption findByName(String name);

        List<TariffOption> getAll();
        
        void updatePrice(int id,int price);

        void updateSubscribeCost(int id,int cost);

        void setIncompatibleOptions(int id, int... optionId);

        void removeIncompatibleOptions(int id, int... optionId);

        void archive(int id);

}
