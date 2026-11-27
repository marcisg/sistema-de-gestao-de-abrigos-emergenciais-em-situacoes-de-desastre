package br.com.arquivo;

import br.com.vo.BoletimVo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BoletimSerializacao {

    private static final String NOME_ARQUIVO = "boletim.bin";

    public static void salvar(BoletimVo boletim) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(boletim);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o boletim: " + e.getMessage());
        }
    }

    public static BoletimVo carregar() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOME_ARQUIVO))) {
            return (BoletimVo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o boletim: " + e.getMessage());
            return null;
        }
    }
}