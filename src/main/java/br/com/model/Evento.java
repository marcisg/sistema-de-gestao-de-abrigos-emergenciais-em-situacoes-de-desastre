package br.com.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String tipo;
    private LocalDate dataInicial = LocalDate.now();
    private LocalDate dataFinal;

    public Evento(String nome, String tipo, LocalDate dataInicial, LocalDate dataFinal) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public Evento() {
    }

    public String getNome() {
        return nome;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Evento\n");
        sb.append("Id: ").append(id);
        sb.append(" | Nome: ").append(nome);
        sb.append(" | Tipo: ").append(tipo);
        sb.append(" | Data Inicial: ").append(dataInicial);
        sb.append(" | Data Final: ").append(dataFinal);
        return sb.toString();
    }
}