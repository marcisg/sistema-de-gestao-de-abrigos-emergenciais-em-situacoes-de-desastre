package br.com.vo;

public class ComparacaoBoletimVo {

    private String nomeAbrigo;
    private int pessoasAntes;
    private int pessoasAgora;

    public ComparacaoBoletimVo(String nomeAbrigo, int pessoasAntes, int pessoasAgora) {
        this.nomeAbrigo = nomeAbrigo;
        this.pessoasAntes = pessoasAntes;
        this.pessoasAgora = pessoasAgora;
    }

    @Override
    public String toString() {
        int diferenca = pessoasAgora - pessoasAntes;
        String tendencia;
        if (diferenca > 0) {
            tendencia = "Subiu " + diferenca;
        } else if (diferenca < 0) {
            tendencia = "Baixou " + (-diferenca);
        } else {
            tendencia = "Sem alteração";
        }
        return "Abrigo: " + nomeAbrigo
                + " | No boletim: " + pessoasAntes
                + " | Agora: " + pessoasAgora
                + " | " + tendencia;
    }
}