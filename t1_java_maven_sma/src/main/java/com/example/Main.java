package com.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String jsonContent = "";
        
        // Solicitar ao usuário que escolha uma opção
        System.out.println("ESCOLHA UMA OPÇÃO:");
        System.out.println("1- T1");
        System.out.println("2- T2");
        System.out.println("3- T2 Melhorado");
        int escolha = scanner.nextInt();

        try {
            switch (escolha) {
                case 1:
                    jsonContent = new String(Files.readAllBytes(Paths.get("./T1.json")));
                    break;
                case 2:
                    jsonContent = new String(Files.readAllBytes(Paths.get("./T2.json")));
                    break;
                    case 3:
                    jsonContent = new String(Files.readAllBytes(Paths.get("./T2Melhorado.json")));
                    break;
                default:
                    System.out.println("Opção inválida. Usando o padrão T1.json");
                    jsonContent = new String(Files.readAllBytes(Paths.get("./T1.json")));
            }

            Gson gson = new Gson();
            Parametros parametros = gson.fromJson(jsonContent, Parametros.class);
            ConfiguracaoFila ambiente = new ConfiguracaoFila(parametros.getFilas(), parametros.getNetwork(), parametros.getArrivals(), parametros.getSeed(), parametros.getQtyOfRandomNumbers());

            while (ambiente.random.hasNext()) {
                ambiente.step();
            }

            for (String FilaId : ambiente.listFilas()) {
                Fila fila = ambiente.getFila(FilaId);
                if (fila != null) {
                    System.out.printf("Fila: %s (Servidores: %d)\n", fila.getId(), fila.getServers());
                    if (fila.getMinArrival() != -1 && fila.getMaxArrival() != -1) {
                        System.out.printf("Tempo de chegada: %.1f - %.1f\n", fila.getMinArrival(), fila.getMaxArrival());
                    }
                    System.out.printf("Tempo de Serviço: %.1f - %.1f\n", fila.getMinDeparture(), fila.getMaxDeparture());
                    System.out.println("------------------------------------------------------");
                    System.out.println("   Estado  |   Tempo Acumulado   |     Probabilidade ");
                    System.out.println("------------------------------------------------------");

                    for (Map.Entry<Integer, Double> entry : fila.getStatistics().entrySet()) {
                        int state = entry.getKey();
                        double time = entry.getValue();
                        double probability = time / (ambiente.getTime() + ambiente.getAccumulatedTime()) * 100;
                        System.out.printf("%9d  | %19.4f | %17.2f%%  | \n", state, time, probability);
                    }

                    System.out.printf("\nPerdas: %d\n", fila.getLost());
                    System.out.println("------------------------------------------------------\n");
                }
            }

            System.out.printf("Tempo de simulação: %.4f\n", ambiente.getTime() + ambiente.getAccumulatedTime());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
