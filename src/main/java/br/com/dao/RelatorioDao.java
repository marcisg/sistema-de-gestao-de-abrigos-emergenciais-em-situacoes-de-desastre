package br.com.dao;

import br.com.vo.BairroAtingidoVo;
import br.com.vo.EncaminhamentoVo;
import jakarta.persistence.EntityManager;

import java.util.List;

public class RelatorioDao {
    private EntityManager em;

    public RelatorioDao(EntityManager em){
        this.em = em;
    }

    public List<BairroAtingidoVo> relatorioBairrosAtingidos(){
        String jpql = "SELECT new br.com.vo.BairroAtingidoVo("
                + "familia.bairroOrigem, "
                + "COUNT(DISTINCT familia.id), "
                + "SUM(acolhimento.quantidadePessoas)) "
                + "FROM Acolhimento acolhimento "
                + "JOIN acolhimento.familia familia "
                + "GROUP BY familia.bairroOrigem "
                + "ORDER BY SUM(acolhimento.quantidadePessoas) DESC";
        return em.createQuery(jpql, BairroAtingidoVo.class)
                .getResultList();
    }

    public List<EncaminhamentoVo> relatorioEncaminhamentos(){
        String jpql = "SELECT new br.com.vo.EncaminhamentoVo("
                + "abrigo.nome, "
                + "COUNT(acolhimento.id), "
                + "SUM(acolhimento.quantidadePessoas)) "
                + "FROM Acolhimento acolhimento "
                + "JOIN acolhimento.abrigo abrigo "
                + "WHERE acolhimento.encaminhada = true "
                + "GROUP BY abrigo.nome "
                + "ORDER BY COUNT(acolhimento.id) DESC";
        return em.createQuery(jpql, EncaminhamentoVo.class)
                .getResultList();
    }
}