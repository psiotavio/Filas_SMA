package com.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.Gson;

public class Main {
     public static void main(String[] args) {
        try {
            // Ler e parsear o arquivo JSON
            String jsonContent = new String(Files.readAllBytes(Paths.get("./model.json")));
            Gson gson = new Gson();
            Parametros parametros = gson.fromJson(jsonContent, Parametros.class);

            // Inicializar o ambiente com os parâmetros lidos
            ConfiguracaoFila ambiente = new ConfiguracaoFila(parametros.getFilas(), parametros.getNetwork(), parametros.getArrivals(), parametros.getSeed(), parametros.getQtyOfRandomNumbers());

            // Executar a simulação
            while (ambiente.random.hasNext()) {
                ambiente.step();
            }

            // Exibir resultados da simulação
            for (String FilaId : ambiente.listFilas()) {
                Fila Fila = ambiente.getFila(FilaId);
                if (Fila != null) {
                    System.out.printf("Fila: %s (Servidores: %d)\n", Fila.getId(), Fila.getServers());
                    if (Fila.getMinArrival() != -1 && Fila.getMaxArrival() != -1) {
                        System.out.printf("Tempo de chegada: %.1f - %.1f\n", Fila.getMinArrival(), Fila.getMaxArrival());
                    }
                    System.out.printf("Tempo de Serviço: %.1f - %.1f\n", Fila.getMinDeparture(), Fila.getMaxDeparture());
                    System.out.println("------------------------------------------------------");
                    System.out.println("   Estado  |   Tempo Acumulado   |     Probabilidade ");
                    System.out.println("------------------------------------------------------");

                    for (Map.Entry<Integer, Double> entry : Fila.getStatistics().entrySet()) {
                        int state = entry.getKey();
                        double time = entry.getValue();
                        double probability = time / (ambiente.getTime() + ambiente.getAccumulatedTime()) * 100;
                        System.out.printf("%9d  | %19.4f | %17.2f%%  | \n", state, time, probability);
                    }

                    System.out.printf("\nPerdas: %d\n", Fila.getLost());
                }
            }

            System.out.printf("Tempo de simulação: %.4f\n", ambiente.getTime() + ambiente.getAccumulatedTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}