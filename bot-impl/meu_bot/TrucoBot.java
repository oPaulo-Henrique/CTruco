package meu_bot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TrucoBot {
    private String name;
    private String perfil;
    private List<Map<String, Object>> historicoJogadas;

    public TrucoBot() {
        this("Bot Truco", "balanceado");
    }

    public TrucoBot(String name, String perfil) {
        this.name = name;
        this.perfil = perfil;
        this.historicoJogadas = new ArrayList<>();
    }

    public void analisarJogada(Object cartaJogada) {
        if (cartaJogada instanceof Map) {
            historicoJogadas.add((Map<String, Object>) cartaJogada);
        } else {
            Map<String, Object> jogada = new HashMap<>();
            jogada.put("carta", Integer.parseInt(cartaJogada.toString()));
            jogada.put("venceu", null);
            historicoJogadas.add(jogada);
        }
    }

    public double calcularMediaJogadas() {
        return historicoJogadas.stream()
                .mapToInt(j -> (int) j.get("carta"))
                .average().orElse(0.0);
    }

    public void registrarResultado(int cartaJogada, boolean venceu) {
        Map<String, Object> jogada = new HashMap<>();
        jogada.put("carta", cartaJogada);
        jogada.put("venceu", venceu);
        historicoJogadas.add(jogada);
        ajustarEstiloComBaseEmPerformance();
    }

    public Map<Integer, Double> taxaVitoriaPorCarta() {
        Map<Integer, int[]> estatisticas = new HashMap<>();
        for (Map<String, Object> entrada : historicoJogadas) {
            int carta = (int) entrada.get("carta");
            boolean venceu = entrada.get("venceu") != null && (boolean) entrada.get("venceu");
            estatisticas.putIfAbsent(carta, new int[2]);
            estatisticas.get(carta)[0]++;
            if (venceu) estatisticas.get(carta)[1]++;
        }

        Map<Integer, Double> taxas = new HashMap<>();
        for (Map.Entry<Integer, int[]> entry : estatisticas.entrySet()) {
            int jogos = entry.getValue()[0];
            int vitorias = entry.getValue()[1];
            taxas.put(entry.getKey(), jogos > 0 ? (double) vitorias / jogos : 0.0);
        }
        return taxas;
    }

    public void ajustarEstiloComBaseEmPerformance() {
        Map<Integer, Double> taxas = taxaVitoriaPorCarta();
        if (taxas.isEmpty()) return;
        double media = taxas.values().stream().mapToDouble(Double::doubleValue).average().orElse(0);
        if (media > 0.7) perfil = "agressivo";
        else if (media < 0.4) perfil = "cauteloso";
        else perfil = "balanceado";
    }

    public int jogarCarta(List<Integer> mao, int rodada) {
        return jogarCarta(mao, rodada, null, 0);
    }

    public int jogarCarta(List<Integer> mao, int rodada, List<Integer> cartasOponente, int pontosRodada) {
        Map<Integer, Double> taxas = taxaVitoriaPorCarta();
        List<Integer> maoOrdenada = new ArrayList<>(mao);
        maoOrdenada.sort((a, b) -> Double.compare(taxas.getOrDefault(b, 0.0), taxas.getOrDefault(a, 0.0)));

        if (rodada == 1 && Collections.max(mao) >= 12) return maoOrdenada.get(0);
        if (rodada == 3 && pontosRodada < 6) return maoOrdenada.get(0);
        if (cartasOponente != null && !cartasOponente.isEmpty() && Collections.max(cartasOponente) >= 12)
            return maoOrdenada.get(mao.size() / 2);
        return maoOrdenada.get(maoOrdenada.size() - 1);
    }

    public boolean aceitarTruco(int pontosRodada, int pontosOponente, List<Integer> mao) {
        int maxCarta = Collections.max(mao);
        return switch (perfil) {
            case "agressivo" -> true;
            case "cauteloso" -> maxCarta >= 12 && pontosRodada >= 8;
            default -> maxCarta >= 11 || pontosRodada >= 10;
        };
    }

    public boolean pedirTruco(int pontosRodada, List<Integer> mao) {
        int maxCarta = Collections.max(mao);
        switch (perfil) {
            case "agressivo":
                return maxCarta >= 10;
            case "cauteloso":
                return maxCarta >= 12 && pontosRodada >= 10;
            default:
                return historicoJogadas.stream().allMatch(j -> (int) j.get("carta") <= 7) || maxCarta >= 12;
        }
    }

    public List<Integer> estimarCartasRestantes(List<Integer> cartasJogadas) {
        Set<Integer> todas = new HashSet<>();
        for (int i = 1; i <= 13; i++) todas.add(i);
        Set<Integer> jogadas = new HashSet<>(cartasJogadas);
        for (Map<String, Object> jogada : historicoJogadas)
            jogadas.add((int) jogada.get("carta"));
        todas.removeAll(jogadas);
        return new ArrayList<>(todas);
    }

    public void resetarHistorico() {
        historicoJogadas.clear();
    }

    public void exportarHistoricoCSV(String caminho) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho))) {
            writer.write("carta,venceu\n");
            for (Map<String, Object> jogada : historicoJogadas) {
                writer.write(jogada.get("carta") + "," + jogada.get("venceu") + "\n");
            }
        }
    }

    public String getPerfil() {
        return perfil;
    }
}
