package br.com.service;

import br.com.dao.AcolhimentoDao;
import br.com.dao.FamiliaDao;
import br.com.model.Familia;
import br.com.model.Membro;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FamiliaService {
    private FamiliaDao familiaDao;
    private AcolhimentoDao acolhimentoDao;

    public FamiliaService(EntityManager em){
        familiaDao = new FamiliaDao(em);
        acolhimentoDao = new AcolhimentoDao(em);
    }

    public void inserir(Familia familia){
        if (familia.getNomeResponsavel() == null || familia.getNomeResponsavel().isBlank()) {
            throw new RuntimeException("O nome do responsável é obrigatório.");
        }
        familiaDao.cadastrar(familia);
    }

    public void alterar(Familia familia){
        familiaDao.atualizar(familia);
    }

    public void excluir(Familia familia){
        if (!acolhimentoDao.buscarPorFamilia(familia.getId()).isEmpty()) {
            throw new RuntimeException("A família possui acolhimentos registrados e não pode ser excluída.");
        }
        familiaDao.remover(familia);
    }

    public void adicionarMembro(Familia familia, Membro membro){
        if (membro.getNome() == null || membro.getNome().isBlank()) {
            throw new RuntimeException("O nome do membro é obrigatório.");
        }
        familia.adicionarMembro(membro);
        familiaDao.cadastrarMembro(membro);
    }

    public void completarCadastro(Familia familia, String cpf, String telefone){
        if (familia.getMembros().isEmpty()) {
            throw new RuntimeException("Cadastre pelo menos um membro antes de completar o cadastro.");
        }
        if (cpf != null && !cpf.isBlank()) {
            long outrasComMesmoCpf = familiaDao.buscarPorCpf(cpf).stream()
                    .filter(outra -> !outra.getId().equals(familia.getId()))
                    .count();
            if (outrasComMesmoCpf > 0) {
                throw new RuntimeException("Já existe outra família cadastrada com este CPF.");
            }
            familia.setCpf(cpf);
        }
        if (telefone != null && !telefone.isBlank()) {
            familia.setTelefone(telefone);
        }
        familia.completarCadastro();
        familiaDao.atualizar(familia);
    }

    public Familia buscarFamiliaPorId(long id){
        return familiaDao.buscarPorId(id);
    }

    public List<Familia> buscarTodasAsFamilias(){
        return familiaDao.buscarTodos();
    }

    public List<Familia> buscarFamiliasPorCpf(String cpf){
        return familiaDao.buscarPorCpf(cpf);
    }

    public List<Familia> buscarFamiliasPorNome(String nome){
        return familiaDao.buscarPorNome(nome);
    }
}