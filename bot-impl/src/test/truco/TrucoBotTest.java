package meu_bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TrucoBotTest {

    private TrucoBot bot;

    @BeforeEach
    public void setUp() {
        bot = new TrucoBot();
    }

    @Test
    public void testJogarCartaPrimeiraRodada() {
        assertEquals(12, bot.jogarCarta(Arrays.asList(3, 7, 12), 1));
        assertEquals(3, bot.jogarCarta(Arrays.asList(3, 7, 10), 1));
    }

    @Test
    public void testPedirTrucoComHistoricoBaixo() {
        bot.analisarJogada("4");
        bot.analisarJogada("5");
        assertTrue(bot.pedirTruco(7, Arrays.asList(2, 4, 6)));
    }

    @Test
    public void testAceitarTrucoAgressivo() {
        TrucoBot botAgressivo = new TrucoBot("Bot Agressivo", "agressivo");
        assertTrue(botAgressivo.aceitarTruco(3, 0, Arrays.asList(3, 5, 9)));
    }

    @Test
    public void testTaxaVitoriaPorCarta() {
        bot.registrarResultado(7, true);
        bot.registrarResultado(7, false);
        bot.registrarResultado(10, true);
        Map<Integer, Double> taxas = bot.taxaVitoriaPorCarta();
        assertEquals(0.5, taxas.get(7), 0.0001);
        assertEquals(1.0, taxas.get(10), 0.0001);
    }

    @Test
    public void testAjusteAdaptativoParaAgressivo() {
        for (int i = 0; i < 8; i++) {
            bot.registrarResultado(10, true);
        }
        assertEquals("agressivo", bot.getPerfil());
    }
}
