package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
@NoArgsConstructor
public class Cart implements CartInterface {
    private Set<Option> options = new HashSet<>();
    private BigDecimal totalSum;
    private int contractId;

    @Override
    public void addItem(Option option) {
        if (options.add(option)) //return true if this set did not already contain the specified element
            totalSum = totalSum.add(option.getSubscribeCost());
    }

    @Override
    public void deleteItem(Option option) {
        if (options.remove(option))
            totalSum = totalSum.subtract(option.getSubscribeCost());
    }

    @Override
    public void clear(){
        options.clear();
        totalSum = BigDecimal.valueOf(0);
    }

    @Override
    public boolean isEmpty(){
        return options.isEmpty();
    }
}
