package br.com.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="abrigos")
public class Abrigo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String endereco;
    private String bairro;
    private int capacidade;

    public Abrigo(String nome, String endereco, String bairro, int capacidade) {
        this.nome = nome;
        this.endereco = endereco;
        this.bairro = bairro;
        this.capacidade = capacidade;
    }

    public Abrigo() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Abrigo\n");
        sb.append("Id: ").append(id);
        sb.append(" | Nome: ").append(nome);
        sb.append(" | Endereço: ").append(endereco);
        sb.append(" | Bairro: ").append(bairro);
        sb.append(" | Capacidade: ").append(capacidade);
        return sb.toString();
    }
    
}
