package services.implementations;

import model.entity.Option;
import model.entity.Tariff;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import services.interfaces.ClientServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;
import services.interfaces.TelegramBotI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Component
@PropertySource("classpath:telegram.properties")
public class TelegramBot implements TelegramBotI {

    @Autowired
    Environment env;
    @Autowired
    OptionServiceI optionService;
    @Autowired
    TariffServiceI tariffServiceI;
    @Autowired
    ClientServiceI clientServiceI;

    @Override
    @Scheduled(fixedRate = 120_000)
    public void generateNews() {
        int newsNum = ThreadLocalRandom.current().nextInt(1, 4); //return 1, 2 or 3
        String message = "";
        switch (newsNum) {
            case 1:
                List<Tariff> tariffs = tariffServiceI.getPaginateData(1, 10).getItems();
                if (!tariffs.isEmpty()) {
                    Tariff tariff = tariffs.get(ThreadLocalRandom.current().nextInt(0, tariffs.size()));
                    message = "Super-duper tariff waiting for you! \nTariff " + tariff.getName() + ": " + tariff.getDescription();
                }
                break;
            case 2:
                List<Option> options = optionService.getPaginateData(1, 10).getItems();
                if (!options.isEmpty()) {
                    Option option = options.get(ThreadLocalRandom.current().nextInt(0, options.size()));
                    message = "Mega-cool option waiting for you! \nOption " + option.getName() + ": " + option.getDescription();
                }
                break;
            case 3:
                int number = 1000 * clientServiceI.getPaginateData(1, 1).getTotal();
                message = "Wow! Already " + number + "clients have joined us!";
                break;
        }
        if (!message.isEmpty()) sendMsg(message);
    }

    @Override
    public int sendMsg(String message) {
        String channel = env.getProperty("CHANNEL_ID");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(env.getProperty("PROXY_IP"), Integer.parseInt(env.getProperty("PROXY_PORT"))));
        String urlString = String.format(env.getProperty("URL"), env.getProperty("API_TOKEN"), channel, message);

        int status = 500;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection(proxy));
            status = connection.getResponseCode();
            Logger.getLogger(TelegramBot.class).info("send message to channel: " + channel + " with response code: " + status);

        } catch (IOException e) {
            Logger.getLogger(TelegramBot.class).info("failed on send message to channel: " + channel);
        }
        return status;
    }
}

