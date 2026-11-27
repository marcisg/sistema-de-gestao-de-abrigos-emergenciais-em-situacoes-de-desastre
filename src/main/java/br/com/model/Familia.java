package br.com.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table (name="familias")
public class Familia {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeResponsavel;
    private String bairroOrigem;
    private String cpf;
    private String telefone;
    private boolean cadastroCompleto = false;
    
    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL)
    private List<Membro> membros = new ArrayList<>();
    
    public Familia(String nomeResponsavel, String bairroOrigem) {
        this.nomeResponsavel = nomeResponsavel;
        this.bairroOrigem = bairroOrigem;
    }

    public Familia() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public String getBairroOrigem() {
        return bairroOrigem;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public boolean isCadastroCompleto() {
        return cadastroCompleto;
    }

    public List<Membro> getMembros() {
        return membros;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public void setBairroOrigem(String bairroOrigem) {
        this.bairroOrigem = bairroOrigem;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void completarCadastro(){
        this.cadastroCompleto = true;
    }

    public void adicionarMembro(Membro membro){
        membro.setFamilia(this);
        this.getMembros().add(membro);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Família\n");
        sb.append("Id: ").append(id);
        sb.append(" | Responsável: ").append(nomeResponsavel);
        sb.append(" | Bairro: ").append(bairroOrigem);
        sb.append(" | CPF: ").append(cpf == null ? "Não informado" : cpf);
        sb.append(" | Telefone: ").append(telefone == null ? "Não informado" : telefone);
        sb.append(" | Cadastro completo: ").append((cadastroCompleto ? "Sim": "Não"));
        sb.append("\n     Membros: ");
        for (Membro membro : this.getMembros()) {
            sb.append(membro).append("\n");
        }
        return sb.toString();
    }
}
