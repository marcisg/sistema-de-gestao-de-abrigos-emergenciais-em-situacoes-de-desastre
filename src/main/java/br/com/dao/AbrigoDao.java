package br.com.dao;

import br.com.model.Abrigo;
import jakarta.persistence.EntityManager;

public class AbrigoDao extends GenericDao<Abrigo> {

    public AbrigoDao(EntityManager em){
        super(em, Abrigo.class);
    }
}