package meu_bot;

public class RunSimulacao {
    public static void main(String[] args) {
        SimuladorPartidas sim = new SimuladorPartidas(100);
        var resultado = sim.simular();
        System.out.println("Vitórias do Bot: " + resultado.get("vitorias_bot"));
        System.out.println("Vitórias do Oponente: " + resultado.get("vitorias_oponente"));
    }
}
