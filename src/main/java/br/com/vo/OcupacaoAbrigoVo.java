package br.com.vo;

import java.io.Serializable;

public class OcupacaoAbrigoVo implements Serializable {

    private String nomeAbrigo;
    private int capacidade;
    private int pessoas;
    private double percentual;
    private String situacao;

    public OcupacaoAbrigoVo(String nomeAbrigo, int capacidade, int pessoas, double percentual, String situacao) {
        this.nomeAbrigo = nomeAbrigo;
        this.capacidade = capacidade;
        this.pessoas = pessoas;
        this.percentual = percentual;
        this.situacao = situacao;
    }

    public String getNomeAbrigo() {
        return nomeAbrigo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getPessoas() {
        return pessoas;
    }

    @Override
    public String toString() {
        return "Abrigo: " + nomeAbrigo
                + " | Capacidade: " + capacidade
                + " | Pessoas: " + pessoas
                + " | Ocupação: " + String.format("%.1f", percentual) + "%"
                + " | Situação: " + situacao;
    }
}