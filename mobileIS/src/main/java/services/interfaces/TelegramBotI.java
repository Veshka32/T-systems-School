package services.interfaces;

public interface TelegramBotI {

    void generateNews();

    int sendMsg(String message);
}
