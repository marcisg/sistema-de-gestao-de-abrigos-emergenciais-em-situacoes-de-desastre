package br.com.service;

import br.com.arquivo.BoletimSerializacao;
import br.com.arquivo.ConfiguracaoArquivo;
import br.com.dao.AbrigoDao;
import br.com.dao.AcolhimentoDao;
import br.com.dao.EventoDao;
import br.com.dao.RelatorioDao;
import br.com.model.Abrigo;
import br.com.model.Acolhimento;
import br.com.model.Evento;
import br.com.model.Familia;
import br.com.model.Membro;
import br.com.vo.AtendimentoEventoVo;
import br.com.vo.BairroAtingidoVo;
import br.com.vo.BoletimVo;
import br.com.vo.EncaminhamentoVo;
import br.com.vo.OcupacaoAbrigoVo;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioService {
    private RelatorioDao relatorioDao;
    private AbrigoDao abrigoDao;
    private EventoDao eventoDao;
    private AcolhimentoDao acolhimentoDao;

    public RelatorioService(EntityManager em){
        relatorioDao = new RelatorioDao(em);
        abrigoDao = new AbrigoDao(em);
        eventoDao = new EventoDao(em);
        acolhimentoDao = new AcolhimentoDao(em);
    }

    public List<OcupacaoAbrigoVo> relatorioOcupacao(){
        int percentualAlerta = ConfiguracaoArquivo.lerPercentualAlerta();
        List<Acolhimento> abertos = acolhimentoDao.buscarAbertos();
        List<OcupacaoAbrigoVo> relatorio = new ArrayList<>();

        for (Abrigo abrigo : abrigoDao.buscarTodos()) {
            int pessoas = abertos.stream()
                    .filter(acolhimento -> acolhimento.getAbrigo().getId().equals(abrigo.getId()))
                    .mapToInt(acolhimento -> acolhimento.getQuantidadePessoas())
                    .sum();
            double percentual = pessoas * 100.0 / abrigo.getCapacidade();

            String situacao;
            if (pessoas > abrigo.getCapacidade()) {
                situacao = "Superlotado";
            } else if (pessoas == abrigo.getCapacidade()) {
                situacao = "Lotado";
            } else if (percentual >= percentualAlerta) {
                situacao = "Quase cheio";
            } else {
                situacao = "Com vaga";
            }

            relatorio.add(new OcupacaoAbrigoVo(abrigo.getNome(), abrigo.getCapacidade(), pessoas, percentual, situacao));
        }
        return relatorio;
    }

    public List<AtendimentoEventoVo> relatorioAtendimentoPorEvento(){
        List<Acolhimento> todos = acolhimentoDao.buscarTodos();
        List<AtendimentoEventoVo> relatorio = new ArrayList<>();

        for (Evento evento : eventoDao.buscarTodos()) {
            List<Acolhimento> doEvento = todos.stream()
                    .filter(acolhimento -> acolhimento.getEvento().getId().equals(evento.getId()))
                    .collect(Collectors.toList());

            List<Familia> familias = doEvento.stream()
                    .map(acolhimento -> acolhimento.getFamilia())
                    .distinct()
                    .collect(Collectors.toList());

            int pessoas = doEvento.stream()
                    .mapToInt(acolhimento -> acolhimento.getQuantidadePessoas())
                    .sum();

            long criancas = familias.stream()
                    .mapToLong(familia -> familia.getMembros().stream()
                            .filter(membro -> calcularIdade(membro) >= 0 && calcularIdade(membro) < 12)
                            .count())
                    .sum();

            long idosos = familias.stream()
                    .mapToLong(familia -> familia.getMembros().stream()
                            .filter(membro -> calcularIdade(membro) >= 60)
                            .count())
                    .sum();

            long pendentes = familias.stream()
                    .filter(familia -> !familia.isCadastroCompleto())
                    .count();

            relatorio.add(new AtendimentoEventoVo(evento.getNome(), familias.size(), pessoas, criancas, idosos, pendentes));
        }
        return relatorio;
    }

    public List<BairroAtingidoVo> relatorioBairrosAtingidos(){
        return relatorioDao.relatorioBairrosAtingidos();
    }

    public List<EncaminhamentoVo> relatorioEncaminhamentos(){
        return relatorioDao.relatorioEncaminhamentos();
    }

    public List<OcupacaoAbrigoVo> relatorioSuperlotados(){
        return relatorioOcupacao().stream()
                .filter(ocupacao -> ocupacao.getPessoas() > ocupacao.getCapacidade())
                .collect(Collectors.toList());
    }

    public void salvarBoletim(){
        BoletimVo boletim = new BoletimVo(LocalDate.now(), relatorioOcupacao());
        BoletimSerializacao.salvar(boletim);
    }

    public BoletimVo carregarBoletim(){
        return BoletimSerializacao.carregar();
    }

    private int calcularIdade(Membro membro){
        if (membro.getDataNascimento() == null) {
            return -1;
        }
        return Period.between(membro.getDataNascimento(), LocalDate.now()).getYears();
    }
}