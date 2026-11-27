package br.com.service;

import br.com.arquivo.ConfiguracaoArquivo;
import br.com.dao.AcolhimentoDao;
import br.com.model.Abrigo;
import br.com.model.Acolhimento;
import br.com.model.Evento;
import br.com.model.Familia;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class AcolhimentoService {
    private AcolhimentoDao acolhimentoDao;
    private FamiliaService familiaService;
    private AbrigoService abrigoService;

    public AcolhimentoService(EntityManager em){
        acolhimentoDao = new AcolhimentoDao(em);
        familiaService = new FamiliaService(em);
        abrigoService = new AbrigoService(em);
    }

    public Acolhimento registrarChegada(Familia familia, Abrigo abrigo, Evento evento, int quantidadePessoas, boolean encaminhada){
        if (quantidadePessoas <= 0) {
            throw new RuntimeException("A quantidade de pessoas deve ser maior que zero.");
        }
        if (familia.getId() == null) {
            familiaService.inserir(familia);
        } else if (!acolhimentoDao.buscarAbertoDaFamilia(familia.getId()).isEmpty()) {
            throw new RuntimeException("A família já possui um acolhimento aberto.");
        }
        Acolhimento acolhimento = new Acolhimento(familia, abrigo, evento, quantidadePessoas, encaminhada);
        acolhimentoDao.cadastrar(acolhimento);
        return acolhimento;
    }

    public void registrarSaida(Acolhimento acolhimento, LocalDate dataSaida){
        if (!acolhimento.estaAberto()) {
            throw new RuntimeException("Este acolhimento já foi encerrado.");
        }
        if (dataSaida.isBefore(acolhimento.getDataEntrada())) {
            throw new RuntimeException("A data de saída não pode ser anterior à data de entrada.");
        }
        acolhimento.registrarSaida(dataSaida);
        acolhimentoDao.atualizar(acolhimento);
    }

    public boolean atingiuAlerta(Abrigo abrigo){
        int percentualAlerta = ConfiguracaoArquivo.lerPercentualAlerta();
        double percentualOcupado = abrigoService.calcularOcupacao(abrigo) * 100.0 / abrigo.getCapacidade();
        return percentualOcupado >= percentualAlerta;
    }

    public void alterar(Acolhimento acolhimento){
        acolhimentoDao.atualizar(acolhimento);
    }

    public Acolhimento buscarAcolhimentoPorId(long id){
        return acolhimentoDao.buscarPorId(id);
    }

    public List<Acolhimento> buscarTodosOsAcolhimentos(){
        return acolhimentoDao.buscarTodos();
    }

    public List<Acolhimento> buscarAcolhidosPorAbrigo(long idAbrigo){
        return acolhimentoDao.buscarAbertosPorAbrigo(idAbrigo);
    }

    public List<Acolhimento> buscarAcolhimentosPorEvento(long idEvento){
        return acolhimentoDao.buscarPorEvento(idEvento);
    }

    public List<Acolhimento> buscarPendentesPorAbrigo(long idAbrigo){
        return acolhimentoDao.buscarPendentesPorAbrigo(idAbrigo);
    }
}