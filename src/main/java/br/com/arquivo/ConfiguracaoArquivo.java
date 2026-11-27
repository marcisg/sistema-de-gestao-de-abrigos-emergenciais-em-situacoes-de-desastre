package br.com.arquivo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfiguracaoArquivo {

    private static final String NOME_ARQUIVO = "configuracao.txt";
    private static final int PERCENTUAL_PADRAO = 80;

    public static int lerPercentualAlerta() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            salvarPercentualAlerta(PERCENTUAL_PADRAO);
            return PERCENTUAL_PADRAO;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha = reader.readLine();
            if (linha == null || !linha.contains("=")) {
                return PERCENTUAL_PADRAO;
            }
            String[] partes = linha.split("=");
            return Integer.parseInt(partes[1].trim());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao ler o arquivo de configuração: " + e.getMessage());
            return PERCENTUAL_PADRAO;
        }
    }

    public static void salvarPercentualAlerta(int percentual) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, false))) {
            writer.write("percentualAlerta=" + percentual);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de configuração: " + e.getMessage());
        }
    }
}
