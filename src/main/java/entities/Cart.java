package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Scope(value = "session",proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
@NoArgsConstructor
public class Cart implements CartInterface {
    Set<TariffOption> options=new HashSet<>();
    int totalSum;

    @Override
    public void addItem(TariffOption option){
        if (options.add(option)) //return true if this set did not already contain the specified element
            totalSum+=option.getSubscribeCost();
    }

    @Override
    public void deleteItem(TariffOption option){
        if (options.remove(option))
            totalSum-=option.getSubscribeCost();
    }

    @Override
    public void clear(){
        options.clear();
        totalSum=0;
    }

    @Override
    public boolean isEmpty(){
        return options.isEmpty();
    }
}
