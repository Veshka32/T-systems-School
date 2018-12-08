package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import services.interfaces.TelegramBotI;

@Controller
public class ManagerCabinet {

    @Autowired
    TelegramBotI telegramBot;

    @RequestMapping("/management/cabinet")
    public String cabinet() {
        return "management/manager-cabinet";
    }

    @RequestMapping("/management")
    public String cabinet1() {
        return "redirect:/management/cabinet";
    }

    @RequestMapping("/management/sendToTelegram")
    @ResponseBody
    public String sendToTelegram(@RequestParam("message") String message) {
        int statusCode = telegramBot.sendMsg(message);
        return statusCode + "";
    }
}
