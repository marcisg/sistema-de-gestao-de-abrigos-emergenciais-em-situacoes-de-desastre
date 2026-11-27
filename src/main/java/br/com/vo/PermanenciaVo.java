package br.com.vo;

public class PermanenciaVo {

    private String nomeAbrigo;
    private long acolhimentos;
    private double mediaDias;
    private long maiorPermanencia;

    public PermanenciaVo(String nomeAbrigo, long acolhimentos, double mediaDias, long maiorPermanencia) {
        this.nomeAbrigo = nomeAbrigo;
        this.acolhimentos = acolhimentos;
        this.mediaDias = mediaDias;
        this.maiorPermanencia = maiorPermanencia;
    }

    @Override
    public String toString() {
        return "Abrigo: " + nomeAbrigo
                + " | Acolhimentos: " + acolhimentos
                + " | Média de permanência: " + String.format("%.1f", mediaDias) + " dias"
                + " | Maior permanência: " + maiorPermanencia + " dias";
    }
}