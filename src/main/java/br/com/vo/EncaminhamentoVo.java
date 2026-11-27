package br.com.vo;

public class EncaminhamentoVo {

    private String nomeAbrigo;
    private Long familiasEncaminhadas;
    private Long pessoas;

    public EncaminhamentoVo(String nomeAbrigo, Long familiasEncaminhadas, Long pessoas) {
        this.nomeAbrigo = nomeAbrigo;
        this.familiasEncaminhadas = familiasEncaminhadas;
        this.pessoas = pessoas;
    }

    @Override
    public String toString() {
        return "Abrigo de destino: " + nomeAbrigo
                + " | Famílias encaminhadas: " + familiasEncaminhadas
                + " | Pessoas: " + pessoas;
    }
}