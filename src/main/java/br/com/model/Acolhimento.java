package br.com.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="acolhimentos")
public class Acolhimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataEntrada = LocalDate.now();
    private LocalDate dataSaida;
    private int quantidadePessoas;
    private boolean encaminhada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familia_id")
    private Familia familia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abrigo_id")
    private Abrigo abrigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    public Acolhimento(Familia familia, Abrigo abrigo, Evento evento, int quantidadePessoas, boolean encaminhada) {
        this.familia = familia;
        this.abrigo = abrigo;
        this.evento = evento;
        this.quantidadePessoas = quantidadePessoas;
        this.encaminhada = encaminhada;
    }

    public Acolhimento() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public boolean isEncaminhada() {
        return encaminhada;
    }

    public Familia getFamilia() {
        return familia;
    }

    public Abrigo getAbrigo() {
        return abrigo;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public void registrarSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public boolean estaAberto() {
        return this.dataSaida == null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Acolhimento\n");
        sb.append("Id: ").append(id);
        sb.append(" | Família: ").append(familia.getNomeResponsavel());
        sb.append(" | Abrigo: ").append(abrigo.getNome());
        sb.append(" | Evento: ").append(evento.getNome());
        sb.append(" | Pessoas: ").append(quantidadePessoas);
        sb.append(" | Entrada: ").append(dataEntrada);
        sb.append(" | Saída: ").append(dataSaida);
        sb.append(" | Encaminhada: ").append((encaminhada ? "Sim" : "Não"));
        return sb.toString();
    }
}