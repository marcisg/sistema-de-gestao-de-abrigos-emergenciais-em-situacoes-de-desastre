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
    private LocalDate dataInicial = LocalDate.now();
    private LocalDate dataFinal = LocalDate.now();

    public Evento(String nome, LocalDate dataInicial, LocalDate dataFinal) {
        this.nome = nome;
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

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setNome(String s) {
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Evento{");
        sb.append("id=").append(id);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", dataInicial=").append(dataInicial);
        sb.append(", dataFinal=").append(dataFinal);
        sb.append('}');
        return sb.toString();
    }
}
