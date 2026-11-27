package br.com.service;

import br.com.dao.AbrigoDao;
import br.com.dao.AcolhimentoDao;
import br.com.model.Abrigo;
import jakarta.persistence.EntityManager;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AbrigoService {
    private AbrigoDao abrigoDao;
    private AcolhimentoDao acolhimentoDao;

    public AbrigoService(EntityManager em){
        abrigoDao = new AbrigoDao(em);
        acolhimentoDao = new AcolhimentoDao(em);
    }

    public void inserir(Abrigo abrigo){
        if (abrigo.getCapacidade() <= 0) {
            throw new RuntimeException("A capacidade do abrigo deve ser maior que zero.");
        }
        abrigoDao.cadastrar(abrigo);
    }

    public void alterar(Abrigo abrigo){
        abrigoDao.atualizar(abrigo);
    }

    public void excluir(Abrigo abrigo){
        if (!acolhimentoDao.buscarPorAbrigo(abrigo.getId()).isEmpty()) {
            throw new RuntimeException("O abrigo possui acolhimentos registrados e não pode ser excluído.");
        }
        abrigoDao.remover(abrigo);
    }

    public Abrigo buscarAbrigoPorId(long id){
        return abrigoDao.buscarPorId(id);
    }

    public List<Abrigo> buscarTodosOsAbrigos(){
        return abrigoDao.buscarTodos();
    }

    public int calcularOcupacao(Abrigo abrigo){
        return acolhimentoDao.buscarAbertosPorAbrigo(abrigo.getId()).stream()
                .mapToInt(acolhimento -> acolhimento.getQuantidadePessoas())
                .sum();
    }

    public boolean cabeNoAbrigo(Abrigo abrigo, int quantidadePessoas){
        return calcularOcupacao(abrigo) + quantidadePessoas <= abrigo.getCapacidade();
    }

    public List<Abrigo> buscarAbrigosComVaga(int quantidadePessoas, String bairro){
        return abrigoDao.buscarTodos().stream()
                .filter(abrigo -> cabeNoAbrigo(abrigo, quantidadePessoas))
                .sorted(Comparator.comparing(abrigo -> !bairro.equalsIgnoreCase(abrigo.getBairro())))
                .collect(Collectors.toList());
    }
}