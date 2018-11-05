package entities.stateful;

import entities.Option;

public interface CartInterface {
    void addItem(Option option);

    void deleteItem(Option option);

    void clear();

    boolean isEmpty();

    java.util.Set<Option> getOptions();

    void setOptions(java.util.Set<Option> options);

    java.math.BigDecimal getTotalSum();

    void setTotalSum(java.math.BigDecimal totalSum);

    int getContractId();

    void setContractId(int contractId);

    void setMessage(String message);
}
