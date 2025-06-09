package meu_bot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.util.Map;

public class GraficoTaxaVitoria {
    public static void plotarTaxaVitoria(TrucoBot bot) {
        Map<Integer, Double> taxas = bot.taxaVitoriaPorCarta();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<Integer, Double> entry : taxas.entrySet()) {
            dataset.addValue(entry.getValue(), "Taxa", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Taxa de Vitória por Carta",
                "Carta", "Taxa de Vitória",
                dataset
        );

        JFrame frame = new JFrame("Gráfico de Desempenho");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}
