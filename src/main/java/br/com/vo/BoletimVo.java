package br.com.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class BoletimVo implements Serializable {

    private LocalDate data;
    private List<OcupacaoAbrigoVo> abrigos;

    public BoletimVo(LocalDate data, List<OcupacaoAbrigoVo> abrigos) {
        this.data = data;
        this.abrigos = abrigos;
    }

    public LocalDate getData() {
        return data;
    }

    public List<OcupacaoAbrigoVo> getAbrigos() {
        return abrigos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Boletim de " + data + "\n");
        for (OcupacaoAbrigoVo abrigo : abrigos) {
            sb.append(abrigo).append("\n");
        }
        return sb.toString();
    }
}