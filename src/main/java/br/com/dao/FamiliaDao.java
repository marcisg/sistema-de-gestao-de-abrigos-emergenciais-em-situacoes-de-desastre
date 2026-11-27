package br.com.dao;

import br.com.model.Familia;
import br.com.model.Membro;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FamiliaDao {
    private EntityManager em;

    public FamiliaDao(EntityManager em){
        this.em = em;
    }

    public void cadastrar(Familia familia){
        this.em.getTransaction().begin();
        this.em.persist(familia);
        this.em.getTransaction().commit();
    }

    public void atualizar(Familia familia){
        this.em.getTransaction().begin();
        this.em.merge(familia);
        this.em.getTransaction().commit();
    }

    public void remover(Familia familia){
        this.em.getTransaction().begin();
        this.em.remove(familia);
        this.em.getTransaction().commit();
    }

    public void cadastrarMembro(Membro membro){
        this.em.getTransaction().begin();
        this.em.persist(membro);
        this.em.getTransaction().commit();
    }

    public void removerMembro(Membro membro){
        this.em.getTransaction().begin();
        this.em.remove(membro);
        this.em.getTransaction().commit();
    }

    public Familia buscarPorId(Long id){
        return em.find(Familia.class, id);
    }

    public List<Familia> buscarTodos(){
        String jpql = "SELECT f FROM Familia f";
        return em.createQuery(jpql, Familia.class).getResultList();
    }

    public List<Familia> buscarPorCpf(String cpf){
        String jpql = "SELECT f FROM Familia f WHERE f.cpf = :cpf";
        return em.createQuery(jpql, Familia.class)
                .setParameter("cpf", cpf)
                .getResultList();
    }

    public List<Familia> buscarPorNome(String nome){
        String jpql = "SELECT f FROM Familia f WHERE LOWER(f.nomeResponsavel) LIKE LOWER(:nome)";
        return em.createQuery(jpql, Familia.class)
                .setParameter("nome", "%" + nome + "%")
                .getResultList();
    }
}