package model.stateful;

import model.entity.Option;

public interface CartInterface {

    String addItem(Option option);

    String deleteItem(Option option);

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
