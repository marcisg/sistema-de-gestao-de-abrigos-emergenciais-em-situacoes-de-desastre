package br.com.vo;

public class AtendimentoEventoVo {

    private String nomeEvento;
    private long familias;
    private long pessoas;
    private long criancas;
    private long idosos;
    private long cadastrosPendentes;

    public AtendimentoEventoVo(String nomeEvento, long familias, long pessoas, long criancas, long idosos, long cadastrosPendentes) {
        this.nomeEvento = nomeEvento;
        this.familias = familias;
        this.pessoas = pessoas;
        this.criancas = criancas;
        this.idosos = idosos;
        this.cadastrosPendentes = cadastrosPendentes;
    }

    @Override
    public String toString() {
        return "Evento: " + nomeEvento
                + " | Famílias: " + familias
                + " | Pessoas: " + pessoas
                + " | Crianças: " + criancas
                + " | Idosos: " + idosos
                + " | Cadastros pendentes: " + cadastrosPendentes;
    }
}