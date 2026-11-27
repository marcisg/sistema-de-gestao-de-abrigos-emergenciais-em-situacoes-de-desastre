package br.com.dao;

import br.com.exception.DataAccessException;
import br.com.model.Acolhimento;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AcolhimentoDao extends GenericDao<Acolhimento> {

    public AcolhimentoDao(EntityManager em){
        super(em, Acolhimento.class);
    }

    public List<Acolhimento> buscarAbertos(){
        try {
            String jpql = "SELECT a FROM Acolhimento a WHERE a.dataSaida IS NULL";
            return em.createQuery(jpql, Acolhimento.class).getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar acolhimentos abertos", e);
        }
    }

    public List<Acolhimento> buscarAbertosPorAbrigo(Long idAbrigo){
        try {
            String jpql = "SELECT a FROM Acolhimento a WHERE a.abrigo.id = :id AND a.dataSaida IS NULL";
            return em.createQuery(jpql, Acolhimento.class)
                    .setParameter("id", idAbrigo)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar acolhimentos abertos do abrigo", e);
        }
    }

    public List<Acolhimento> buscarAbertoDaFamilia(Long idFamilia){
        try {
            String jpql = "SELECT a FROM Acolhimento a WHERE a.familia.id = :id AND a.dataSaida IS NULL";
            return em.createQuery(jpql, Acolhimento.class)
                    .setParameter("id", idFamilia)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar acolhimento aberto da família", e);
        }
    }

    public List<Acolhimento> buscarPorEvento(Long idEvento){
        try {
            String jpql = "SELECT a FROM Acolhimento a WHERE a.evento.id = :id";
            return em.createQuery(jpql, Acolhimento.class)
                    .setParameter("id", idEvento)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar acolhimentos do evento", e);
        }
    }

    public List<Acolhimento> buscarPorAbrigo(Long idAbrigo){
        try {
            String jpql = "SELECT a FROM Acolhimento a WHERE a.abrigo.id = :id";
            return em.createQuery(jpql, Acolhimento.class)
                    .setParameter("id", idAbrigo)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar acolhimentos do abrigo", e);
        }
    }

    public List<Acolhimento> buscarPorFamilia(Long idFamilia){
        try {
            String jpql = "SELECT a FROM Acolhimento a WHERE a.familia.id = :id";
            return em.createQuery(jpql, Acolhimento.class)
                    .setParameter("id", idFamilia)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar acolhimentos da família", e);
        }
    }

    public List<Acolhimento> buscarPendentesPorAbrigo(Long idAbrigo){
        try {
            String jpql = "SELECT a FROM Acolhimento a WHERE a.abrigo.id = :id AND a.dataSaida IS NULL AND a.familia.cadastroCompleto = false";
            return em.createQuery(jpql, Acolhimento.class)
                    .setParameter("id", idAbrigo)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar cadastros pendentes do abrigo", e);
        }
    }
}