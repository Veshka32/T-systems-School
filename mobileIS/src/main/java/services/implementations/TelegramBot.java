package services.implementations;

import model.entity.Option;
import model.entity.Tariff;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TelegramBot implements TelegramBotI {

    private static final String URL = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    private static final String API_TOKEN = "694707256:AAGUOh-rjaC2GN9d8EbGDpd10AZar-Vhwp8";
    private static final String CHANNEL_ID = "@MultiverseMobile";
    private static final String PROXY_IP = "86.57.223.177";
    private static final int PROXY_PORT = 41096;
    private static final int MILLISEC = 120_000;
    @Autowired
    OptionServiceI optionService;
    @Autowired
    TariffServiceI tariffServiceI;
    @Autowired
    ClientServiceI clientServiceI;

    @Override
    @Scheduled(fixedRate = MILLISEC)
    public void generateNews() {
        int newsNum = ThreadLocalRandom.current().nextInt(1, 4); //return 1, 2 or 3
        String message = "";
        switch (newsNum) {
            case 1:
                List<Tariff> tariffs = tariffServiceI.getPaginateData(1, 10).getItems();
                Tariff tariff = tariffs.get(ThreadLocalRandom.current().nextInt(0, tariffs.size()));
                message = "Super-duper tariff waiting for you! \nTariff " + tariff.getName() + ": " + tariff.getDescription();
                break;
            case 2:
                List<Option> options = optionService.getPaginateData(1, 10).getItems();
                Option option = options.get(ThreadLocalRandom.current().nextInt(0, options.size()));
                message = "Mega-cool option waiting for you! \nOption " + option.getName() + ": " + option.getDescription();
                break;
            case 3:
                int number = 1000 * clientServiceI.getPaginateData(1, 1).getTotal();
                message = "Wow! Already " + number + "clients have joined us!";
        }
        sendMsg(message);
    }

    @Override
    public int sendMsg(String message) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_IP, PROXY_PORT));
        String urlString = String.format(URL, API_TOKEN, CHANNEL_ID, message);
        int status = 500;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection(proxy));
            status = connection.getResponseCode();
            Logger.getLogger(TelegramBot.class).info("send message to channel: " + CHANNEL_ID + " with response code: " + status);

        } catch (IOException e) {
            Logger.getLogger(TelegramBot.class).info("failed on send message to channel: " + CHANNEL_ID);
        }
        return status;


//        String urlString=String.format(URL,API_TOKEN);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlString)
//                .queryParam("chat_id", CHANNEL_ID)
//                .queryParam("text", "Stas, Hello!");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                entity,
//                String.class);
//
//            if ((response).getStatusCode().equals(HttpStatus.OK))
//                Logger.getLogger(TelegramBot.class).info("Message was successfully sent to telegram channel "+ CHANNEL_ID);
//            else
//                Logger.getLogger(TelegramBot.class).warn("Message was sent with response status: "+response.getStatusCode());
//        }
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://api.telegram.org/bot694707256:AAGUOh-rjaC2GN9d8EbGDpd10AZar-Vhwp8/sendMessage?chat_id=@MultiverseMobile&text=TEST", String.class);


    }
}

