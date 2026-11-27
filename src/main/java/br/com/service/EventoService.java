package br.com.service;

import br.com.dao.AcolhimentoDao;
import br.com.dao.EventoDao;
import br.com.model.Evento;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class EventoService {
    private EventoDao eventoDao;
    private AcolhimentoDao acolhimentoDao;

    public EventoService(EntityManager em){
        eventoDao = new EventoDao(em);
        acolhimentoDao = new AcolhimentoDao(em);
    }

    public void inserir(Evento evento){
        if (evento.getDataFinal() != null && evento.getDataFinal().isBefore(evento.getDataInicial())) {
            throw new RuntimeException("A data final não pode ser anterior à data inicial.");
        }
        eventoDao.cadastrar(evento);
    }

    public void alterar(Evento evento){
        eventoDao.atualizar(evento);
    }

    public void encerrar(Evento evento, LocalDate dataFinal){
        if (dataFinal.isBefore(evento.getDataInicial())) {
            throw new RuntimeException("A data final não pode ser anterior à data inicial.");
        }
        evento.setDataFinal(dataFinal);
        eventoDao.atualizar(evento);
    }

    public void excluir(Evento evento){
        if (!acolhimentoDao.buscarPorEvento(evento.getId()).isEmpty()) {
            throw new RuntimeException("O evento possui acolhimentos registrados e não pode ser excluído.");
        }
        eventoDao.remover(evento);
    }

    public Evento buscarEventoPorId(long id){
        return eventoDao.buscarPorId(id);
    }

    public List<Evento> buscarTodosOsEventos(){
        return eventoDao.buscarTodos();
    }

    public List<Evento> buscarEventosAtivos(){
        return eventoDao.buscarAtivos();
    }
}