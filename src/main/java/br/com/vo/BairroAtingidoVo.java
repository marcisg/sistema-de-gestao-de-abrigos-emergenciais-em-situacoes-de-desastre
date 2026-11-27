package br.com.vo;

public class BairroAtingidoVo {

    private String bairro;
    private Long familias;
    private Long pessoas;

    public BairroAtingidoVo(String bairro, Long familias, Long pessoas) {
        this.bairro = bairro;
        this.familias = familias;
        this.pessoas = pessoas;
    }

    @Override
    public String toString() {
        return "Bairro: " + (bairro == null ? "Não informado" : bairro)
                + " | Famílias: " + familias
                + " | Pessoas: " + pessoas;
    }
}