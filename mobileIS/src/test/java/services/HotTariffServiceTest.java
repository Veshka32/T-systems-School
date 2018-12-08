package services;

import model.entity.Tariff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import services.implementations.HotTariffService;
import services.interfaces.JmsSenderI;
import services.interfaces.TariffServiceI;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotTariffServiceTest {
    @InjectMocks
    private HotTariffService hotTariffService;

    @Mock
    private JmsSenderI jmsSender;

    @Mock
    private TariffServiceI tariffServiceI;

    @BeforeEach
    void setUp() {
        //enable mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void pushHots() {
        Tariff t = new Tariff();
        t.setName("test");
        t.setPrice(new BigDecimal(5));
        when(tariffServiceI.getLast(3)).thenReturn(Collections.singletonList(t));
        assertDoesNotThrow(() -> hotTariffService.pushHots());
        Mockito.verify(jmsSender, times(1)).sendData("[{\"name\":\"test\",\"price\":5}]");
    }

    @Test
    void pushIfHots() {
        Tariff t = new Tariff();
        t.setId(1); //hot
        Tariff t1 = new Tariff();
        t1.setId(2);//hot
        when(tariffServiceI.getLast(3)).thenReturn(Arrays.asList(t, t1));

        //must perform pushHots()
        hotTariffService.pushIfHots(1);
        Mockito.verify(jmsSender, times(1)).sendData("[{},{}]");

        //must not perform pushHosts()
        t.setName("test");
        hotTariffService.pushIfHots(3);
        Mockito.verify(jmsSender, times(0)).sendData("[{\"name\":\"test\"},{}]");
    }
}