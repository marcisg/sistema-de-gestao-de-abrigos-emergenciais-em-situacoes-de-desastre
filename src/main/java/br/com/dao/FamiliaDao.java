package br.com.dao;

import br.com.exception.DataAccessException;
import br.com.model.Familia;
import br.com.model.Membro;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FamiliaDao extends GenericDao<Familia> {

    public FamiliaDao(EntityManager em){
        super(em, Familia.class);
    }

    public void cadastrarMembro(Membro membro){
        try {
            this.em.getTransaction().begin();
            this.em.persist(membro);
            this.em.getTransaction().commit();
        } catch (Exception e) {
            this.em.getTransaction().rollback();
            throw new DataAccessException("Erro ao cadastrar o membro: " + membro.getNome(), e);
        }
    }

    public List<Familia> buscarPorCpf(String cpf){
        try {
            String jpql = "SELECT f FROM Familia f WHERE f.cpf = :cpf";
            return em.createQuery(jpql, Familia.class)
                    .setParameter("cpf", cpf)
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar família por CPF: " + cpf, e);
        }
    }

    public List<Familia> buscarPorNome(String nome){
        try {
            String jpql = "SELECT f FROM Familia f WHERE LOWER(f.nomeResponsavel) LIKE LOWER(:nome)";
            return em.createQuery(jpql, Familia.class)
                    .setParameter("nome", "%" + nome + "%")
                    .getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar família por nome: " + nome, e);
        }
    }
}