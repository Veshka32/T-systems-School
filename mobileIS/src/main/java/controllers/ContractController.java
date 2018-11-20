package controllers;

import model.dto.ContractDTO;
import model.entity.Contract;
import model.helpers.PaginateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.exceptions.ServiceException;
import services.interfaces.ClientServiceI;
import services.interfaces.ContractServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

@Controller
public class ContractController {

    private static final String MODEL_MESSAGE = "message";
    private static final String CONTRACT = "contract";
    private static final String EDIT = "management/contract/create-contract";
    private static final String MANAGEMENT="management/contract/contract-management";
    private static final String REDIRECT_SHOW = "redirect:/management/showContract";
    private static final int ROW_PER_PAGE = 3; //specify number of rows per page in the table with all contracts

    @Autowired
    ContractServiceI contractService;
    @Autowired
    TariffServiceI tariffService;
    @Autowired
    OptionServiceI optionService;
    @Autowired
    ClientServiceI clientService;

    @GetMapping("management/findContract")
    @ResponseBody
    public String findByAjax(@RequestParam(value = "phone", required = false) String phone) {
        return contractService.getJson(phone);
    }

    @RequestMapping("/management/contracts")
    public String showAll(@RequestParam(value = "page", required = false) Integer page, Model model) {
        buildModel(page, model);
        return MANAGEMENT;
    }

    @GetMapping("/management/showContract")
    public String show(@RequestParam("id") int id, Model model) {
        model.addAttribute(CONTRACT, contractService.getDTO(id));
        return "management/contract/show-contract";
    }

    @GetMapping("/management/createContract")
    public String create(@RequestParam("clientId") int clientId, Model model) {
        ContractDTO dto = new ContractDTO(clientId);
        contractService.addData(dto);
        model.addAttribute(CONTRACT, dto);
        return EDIT;
    }

    @PostMapping("management/createContract")
    public String create(@ModelAttribute(CONTRACT) ContractDTO dto, Model model, RedirectAttributes attr) {
        try {
            attr.addAttribute("id", contractService.create(dto));
            return REDIRECT_SHOW;

        } catch (ServiceException e) {
            model.addAttribute(MODEL_MESSAGE, e.getMessage());
            contractService.addData(dto);
            model.addAttribute(CONTRACT, dto);
            return EDIT;
        }
    }

    @GetMapping("/management/editContract")
    public String edit(@RequestParam("id") int id, Model model) {
        ContractDTO dto = contractService.getDTO(id);
        contractService.addData(dto);
        model.addAttribute(CONTRACT, dto);
        return EDIT;
    }

    @PostMapping("/management/editContract")
    public String edit(@ModelAttribute(CONTRACT) ContractDTO dto, Model model, RedirectAttributes attr) {
        try {
            contractService.update(dto);
            attr.addAttribute("id", dto.getId());
            return REDIRECT_SHOW;
        } catch (ServiceException e) {
            model.addAttribute(MODEL_MESSAGE,e.getMessage());
            contractService.addData(dto);
            model.addAttribute(CONTRACT, dto);
            return EDIT;
        }
    }

    @PostMapping("/management/deleteContract")
    public String delete(@RequestParam("id") int id, @RequestParam("clientId") int clientId, RedirectAttributes attr) {
        contractService.delete(id);
        attr.addAttribute("id",clientId);
        return "redirect:/management/showClient";
    }

    private void buildModel(Integer page, Model model) {
        PaginateHelper<Contract> helper = contractService.getPaginateData(page, ROW_PER_PAGE);
        model.addAttribute("allInPage", helper.getItems());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTotal", helper.getTotal());
    }
}
