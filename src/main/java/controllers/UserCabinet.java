package controllers;

import entities.Contract;
import entities.Tariff;
import entities.TariffOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.ClientService;
import services.ContractService;
import services.ServiceException;
import services.TariffServiceI;

import javax.sql.rowset.serial.SerialException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserCabinet {
    @Autowired
    ContractService contractService;

    @Autowired
    TariffServiceI tariffServiceI;

    @RequestMapping("/user/cabinet")
    public String create(Model model, Principal user){
        Contract contract=contractService.getFull(Long.parseLong(user.getName()));
        model.addAttribute("contract",contract);
        model.addAttribute("tariffOptions",contract.getTariff().getOptions());
        model.addAttribute("contractOptions",contract.getOptions());
        return "user/user-cabinet";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/user/block")
    public String blockContract(Principal user){
        contractService.block(Long.parseLong(user.getName()));
        return "redirect:/user/cabinet";
    }

    @GetMapping("/user/unblock")
    public String unblockContract(Principal user){
        contractService.unblock(Long.parseLong(user.getName()));
        return "redirect:/user/cabinet";
    }

    @GetMapping("/user/changeTariff")
    public String changeTariff(Principal user){
        return "redirect:/user/cabinet/tariffsToChange";
    }

    @GetMapping("/user/deleteOption/{optionId}")
    public String deleteOption(Principal user, @PathVariable int optionId, Model model){
        try{contractService.deleteOption(Long.parseLong(user.getName()),optionId);}
        catch (ServiceException e){
            model.addAttribute("message",e.getMessage());
        }
        finally {
            Contract contract=contractService.getFull(Long.parseLong(user.getName()));
            model.addAttribute("contract",contract);
            model.addAttribute("tariffOptions",contract.getTariff().getOptions());
            model.addAttribute("contractOptions",contract.getOptions());
            return "user/user-cabinet";
        }
    }
}
