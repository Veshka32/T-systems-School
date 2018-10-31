package entities;

import java.util.Set;

public interface CartInterface {
    void addItem(TariffOption option);

    void deleteItem(TariffOption option);

    void clear();

    boolean isEmpty();

    void setOptions(java.util.Set<TariffOption> options);

    void setTotalSum(int totalSum);

    Set<TariffOption> getOptions();

    int getTotalSum();

    int getContractId();

    void setContractId(int id);
}