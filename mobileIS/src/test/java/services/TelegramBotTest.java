package services;

import model.entity.Client;
import model.entity.Option;
import model.entity.Tariff;
import model.helpers.PaginateHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import services.implementations.TelegramBot;
import services.interfaces.ClientServiceI;
import services.interfaces.OptionServiceI;
import services.interfaces.TariffServiceI;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock
    OptionServiceI optionService;
    @Mock
    TariffServiceI tariffServiceI;
    @Mock
    ClientServiceI clientServiceI;

    @InjectMocks
    TelegramBot bot;

    @BeforeEach
    void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
        //because of random flow of method
    void generateNews() {

        PaginateHelper<Tariff> helper = new PaginateHelper<>(new ArrayList<>(), 10);
        when(tariffServiceI.getPaginateData(1, 10)).thenReturn(helper);
        PaginateHelper<Option> helper1 = new PaginateHelper<>(new ArrayList<>(), 10);
        when(optionService.getPaginateData(1, 10)).thenReturn(helper1);
        PaginateHelper<Client> helper2 = new PaginateHelper<>(new ArrayList<>(), 10);
        when(clientServiceI.getPaginateData(1, 1)).thenReturn(helper2);
        assertDoesNotThrow(() -> bot.generateNews());

        helper.getItems().add(new Tariff());
        helper1.getItems().add(new Option());
        helper2.getItems().add(new Client());
        assertThrows(NullPointerException.class, () -> bot.generateNews()); //message is sent, but method sendMessage() is not mocked
    }
}