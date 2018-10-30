package controllers;

import entities.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import services.*;
import services.implementations.ContractService;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import java.security.Principal;

@Controller
public class UserCabinet {
    @Autowired
    ContractService contractService;

    @Autowired
    TariffServiceI tariffServiceI;

    @Autowired
    OptionServiceI optionServiceI;

    @RequestMapping("/user/cabinet")
    public String create(Model model, Principal user){
        Contract contract=contractService.getFull(Long.parseLong(user.getName()));
        model.addAttribute("contract",contract);
        model.addAttribute("tariffOptions",contract.getTariff().getOptions());
        model.addAttribute("contractOptions",contract.getOptions());
        model.addAttribute("availableOptions",optionServiceI.getAll());
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

    @GetMapping("/user/showTariffs")
    public String changeTariff(Model model){
        model.addAttribute("allTariffs",tariffServiceI.getAll());
        return "user/user-tariffs";
    }

    @GetMapping("/user/getTariff/{tariffId}")
    public String getTariff(Principal user, @PathVariable int tariffId, Model model){
        contractService.setTariff(Long.parseLong(user.getName()),tariffId);
        return "redirect:/user/cabinet";
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
