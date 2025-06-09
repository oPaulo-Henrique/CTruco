package meu_bot;

import java.util.*;

public class SimuladorPartidas {
    private int nPartidas;
    private Map<String, Integer> resultados;

    public SimuladorPartidas(int nPartidas) {
        this.nPartidas = nPartidas;
        this.resultados = new HashMap<>();
        resultados.put("vitorias_bot", 0);
        resultados.put("vitorias_oponente", 0);
    }

    public Map<String, Integer> simular() {
        Random rand = new Random();

        for (int i = 0; i < nPartidas; i++) {
            TrucoBot bot = new TrucoBot();
            TrucoBot oponente = new TrucoBot("Bot Cauteloso", "cauteloso");

            int vitoriasBot = 0;
            int vitoriasOponente = 0;

            for (int rodada = 0; rodada < 3; rodada++) {
                List<Integer> maoBot = gerarMao(rand);
                List<Integer> maoOponente = gerarMao(rand);

                int cartaBot = bot.jogarCarta(maoBot, rodada + 1);
                int cartaOp = oponente.jogarCarta(maoOponente, rodada + 1);

                if (cartaBot > cartaOp) {
                    vitoriasBot++;
                    bot.registrarResultado(cartaBot, true);
                } else {
                    vitoriasOponente++;
                    bot.registrarResultado(cartaBot, false);
                }
            }

            if (vitoriasBot > vitoriasOponente) {
                resultados.put("vitorias_bot", resultados.get("vitorias_bot") + 1);
            } else {
                resultados.put("vitorias_oponente", resultados.get("vitorias_oponente") + 1);
            }
        }
        return resultados;
    }

    private List<Integer> gerarMao(Random rand) {
        List<Integer> cartas = new ArrayList<>();
        while (cartas.size() < 3) {
            int carta = rand.nextInt(13) + 1;
            if (!cartas.contains(carta)) cartas.add(carta);
        }
        return cartas;
    }
}
