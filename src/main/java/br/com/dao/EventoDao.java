package br.com.dao;

import br.com.exception.DataAccessException;
import br.com.model.Evento;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EventoDao extends GenericDao<Evento> {

    public EventoDao(EntityManager em){
        super(em, Evento.class);
    }

    public List<Evento> buscarAtivos(){
        try {
            String jpql = "SELECT e FROM Evento e WHERE e.dataFinal IS NULL";
            return em.createQuery(jpql, Evento.class).getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar eventos ativos", e);
        }
    }
}