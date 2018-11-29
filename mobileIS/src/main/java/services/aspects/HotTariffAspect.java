package services.aspects;

import model.dto.TariffDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.interfaces.HotTariffServiceI;

import java.util.Optional;


@Aspect
@Component
public class HotTariffAspect {


    @Autowired
    HotTariffServiceI hotTariffService;

    @AfterReturning(value = "execution(* services.implementations.TariffService.create(..)) " +
            "|| execution(* services.implementations.TariffService.update(..)) ", returning = "returnValue")
    public void notifyHotTariffService(JoinPoint point, Object returnValue) {
        Optional<String> error = (Optional<String>) returnValue;
        if (!error.isPresent()) {
            Object args = point.getArgs()[0];
            if (args instanceof TariffDTO) hotTariffService.pushIfHots(((TariffDTO) args).getId());
        }
    }

    @AfterReturning(value = "execution(* services.implementations.TariffService.delete(..))", returning = "returnValue")
    public void notifyHotTariffServiceAboutDelete(JoinPoint point, Object returnValue) {
        Optional<String> error = (Optional<String>) returnValue;
        if (!error.isPresent()) {
            int args = (int) point.getArgs()[0];
            hotTariffService.pushIfHots(args);
        }
    }
}
