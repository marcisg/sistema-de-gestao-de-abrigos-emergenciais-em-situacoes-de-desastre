package br.com;

import br.com.arquivo.ConfiguracaoArquivo;
import br.com.model.Abrigo;
import br.com.model.Acolhimento;
import br.com.model.Evento;
import br.com.model.Familia;
import br.com.model.Membro;
import br.com.service.AbrigoService;
import br.com.service.AcolhimentoService;
import br.com.service.EventoService;
import br.com.service.FamiliaService;
import br.com.service.RelatorioService;
import br.com.vo.BoletimVo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static EntityManager em;

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PostgresPU");
        em = emf.createEntityManager();

        EventoService eventoService = new EventoService(em);
        AbrigoService abrigoService = new AbrigoService(em);
        FamiliaService familiaService = new FamiliaService(em);
        AcolhimentoService acolhimentoService = new AcolhimentoService(em);
        RelatorioService relatorioService = new RelatorioService(em);

        boolean continuar = true;

        while (continuar) {
            System.out.println();
            System.out.println("----- MENU -----");
            System.out.println("EVENTOS");
            System.out.println("1. Cadastrar Evento");
            System.out.println("2. Encerrar Evento");
            System.out.println("3. Excluir Evento");
            System.out.println("4. Listar Eventos");
            System.out.println("ABRIGOS");
            System.out.println("5. Cadastrar Abrigo");
            System.out.println("6. Alterar Abrigo");
            System.out.println("7. Excluir Abrigo");
            System.out.println("8. Listar Abrigos com ocupação");
            System.out.println("FAMÍLIAS E ACOLHIMENTOS");
            System.out.println("9. Registrar chegada");
            System.out.println("10. Completar cadastro da família");
            System.out.println("11. Buscar família por nome ou CPF");
            System.out.println("12. Excluir família");
            System.out.println("13. Registrar saída");
            System.out.println("14. Listar acolhidos por abrigo");
            System.out.println("15. Listar cadastros pendentes por abrigo");
            System.out.println("RELATÓRIOS");
            System.out.println("16. Ocupação dos abrigos");
            System.out.println("17. Atendimento por evento");
            System.out.println("18. Bairros mais atingidos");
            System.out.println("19. Encaminhamentos e superlotação");
            System.out.println("ARQUIVOS");
            System.out.println("20. Salvar boletim do dia");
            System.out.println("21. Ver boletim salvo");
            System.out.println("22. Configurar percentual de alerta de lotação");
            System.out.println("0. Sair");

            int opcao = lerInteiro("Escolha uma opção: ", 0, 22);

            try {
                switch (opcao) {
                    case 1 -> cadastrarEvento(eventoService);
                    case 2 -> encerrarEvento(eventoService);
                    case 3 -> excluirEvento(eventoService);
                    case 4 -> listarEventos(eventoService);
                    case 5 -> cadastrarAbrigo(abrigoService);
                    case 6 -> alterarAbrigo(abrigoService);
                    case 7 -> excluirAbrigo(abrigoService);
                    case 8 -> listarAbrigos(abrigoService);
                    case 9 -> registrarChegada(acolhimentoService, abrigoService, eventoService, familiaService);
                    case 10 -> completarCadastro(familiaService);
                    case 11 -> buscarFamilia(familiaService);
                    case 12 -> excluirFamilia(familiaService);
                    case 13 -> registrarSaida(acolhimentoService, abrigoService);
                    case 14 -> listarAcolhidosPorAbrigo(acolhimentoService, abrigoService);
                    case 15 -> listarPendentesPorAbrigo(acolhimentoService, abrigoService);
                    case 16 -> relatorioService.relatorioOcupacao().forEach(System.out::println);
                    case 17 -> relatorioService.relatorioAtendimentoPorEvento().forEach(System.out::println);
                    case 18 -> relatorioService.relatorioBairrosAtingidos().forEach(System.out::println);
                    case 19 -> relatorioEncaminhamentos(relatorioService);
                    case 20 -> salvarBoletim(relatorioService);
                    case 21 -> verBoletim(relatorioService);
                    case 22 -> configurarPercentualAlerta();
                    case 0 -> continuar = false;
                }
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                System.out.println("Erro: " + e.getMessage());
            }
        }

        em.close();
        emf.close();
        System.out.println("Programa encerrado.");
    }

    private static void cadastrarEvento(EventoService eventoService) {
        String nome = lerTexto("Nome do evento (ex.: Enchente nov/2026): ");
        String tipo = lerTipoEvento();
        LocalDate dataInicial = lerDataOpcional("Data inicial (aaaa-mm-dd, Enter para hoje): ");
        if (dataInicial == null) {
            dataInicial = LocalDate.now();
        }
        eventoService.inserir(new Evento(nome, tipo, dataInicial, null));
        System.out.println("Evento cadastrado com sucesso!");
    }

    private static void encerrarEvento(EventoService eventoService) {
        Evento evento = eventoService.buscarEventoPorId(lerLong("ID do evento a ser encerrado: "));
        if (evento == null || evento.getDataFinal() != null) {
            System.out.println("Evento não encontrado ou já encerrado.");
            return;
        }
        LocalDate dataFinal = lerDataOpcional("Data final (aaaa-mm-dd, Enter para hoje): ");
        if (dataFinal == null) {
            dataFinal = LocalDate.now();
        }
        eventoService.encerrar(evento, dataFinal);
        System.out.println("Evento encerrado com sucesso!");
    }

    private static void excluirEvento(EventoService eventoService) {
        Evento evento = eventoService.buscarEventoPorId(lerLong("ID do evento a ser excluído: "));
        if (evento == null) {
            System.out.println("Evento não encontrado.");
            return;
        }
        eventoService.excluir(evento);
        System.out.println("Evento excluído com sucesso!");
    }

    private static void listarEventos(EventoService eventoService) {
        List<Evento> eventos = eventoService.buscarTodosOsEventos();
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
        }
        eventos.forEach(System.out::println);
    }

    private static void cadastrarAbrigo(AbrigoService abrigoService) {
        String nome = lerTexto("Nome do abrigo: ");
        String endereco = lerTexto("Endereço: ");
        String bairro = lerTexto("Bairro: ");
        int capacidade = lerInteiro("Capacidade (número de pessoas): ", 1, Integer.MAX_VALUE);
        abrigoService.inserir(new Abrigo(nome, endereco, bairro, capacidade));
        System.out.println("Abrigo cadastrado com sucesso!");
    }

    private static void alterarAbrigo(AbrigoService abrigoService) {
        Abrigo abrigo = abrigoService.buscarAbrigoPorId(lerLong("ID do abrigo a ser alterado: "));
        if (abrigo == null) {
            System.out.println("Abrigo não encontrado.");
            return;
        }
        System.out.println(abrigo);
        String nome = lerTextoOpcional("Novo nome (Enter para manter): ");
        if (nome != null) {
            abrigo.setNome(nome);
        }
        String endereco = lerTextoOpcional("Novo endereço (Enter para manter): ");
        if (endereco != null) {
            abrigo.setEndereco(endereco);
        }
        String bairro = lerTextoOpcional("Novo bairro (Enter para manter): ");
        if (bairro != null) {
            abrigo.setBairro(bairro);
        }
        int capacidade = lerInteiro("Nova capacidade (0 para manter): ", 0, Integer.MAX_VALUE);
        if (capacidade > 0) {
            abrigo.setCapacidade(capacidade);
        }
        abrigoService.alterar(abrigo);
        System.out.println("Abrigo alterado com sucesso!");
    }

    private static void excluirAbrigo(AbrigoService abrigoService) {
        Abrigo abrigo = abrigoService.buscarAbrigoPorId(lerLong("ID do abrigo a ser excluído: "));
        if (abrigo == null) {
            System.out.println("Abrigo não encontrado.");
            return;
        }
        abrigoService.excluir(abrigo);
        System.out.println("Abrigo excluído com sucesso!");
    }

    private static void listarAbrigos(AbrigoService abrigoService) {
        List<Abrigo> abrigos = abrigoService.buscarTodosOsAbrigos();
        if (abrigos.isEmpty()) {
            System.out.println("Nenhum abrigo cadastrado.");
        }
        for (Abrigo abrigo : abrigos) {
            System.out.println(abrigo);
            System.out.println("Ocupação atual: " + abrigoService.calcularOcupacao(abrigo) + "/" + abrigo.getCapacidade());
        }
    }

    private static void registrarChegada(AcolhimentoService acolhimentoService, AbrigoService abrigoService, EventoService eventoService, FamiliaService familiaService) {
        List<Evento> eventosAtivos = eventoService.buscarEventosAtivos();
        if (eventosAtivos.isEmpty()) {
            System.out.println("Nenhum evento ativo. Cadastre um evento antes de registrar a chegada.");
            return;
        }
        eventosAtivos.forEach(System.out::println);
        Evento evento = eventoService.buscarEventoPorId(lerLong("ID do evento: "));
        if (evento == null || evento.getDataFinal() != null) {
            System.out.println("Evento não encontrado ou já encerrado.");
            return;
        }

        listarAbrigos(abrigoService);
        Abrigo abrigo = abrigoService.buscarAbrigoPorId(lerLong("ID do abrigo onde a família chegou: "));
        if (abrigo == null) {
            System.out.println("Abrigo não encontrado.");
            return;
        }

        Familia familia;
        if (lerSimNao("A família já possui cadastro? (S/N): ")) {
            familia = familiaService.buscarFamiliaPorId(lerLong("ID da família: "));
            if (familia == null) {
                System.out.println("Família não encontrada.");
                return;
            }
        } else {
            familia = new Familia(lerTexto("Nome do responsável: "), lerTexto("Bairro de origem: "));
        }
        int quantidadePessoas = lerInteiro("Quantidade de pessoas que chegaram: ", 1, Integer.MAX_VALUE);

        Abrigo abrigoDestino = abrigo;
        boolean encaminhada = false;

        if (!abrigoService.cabeNoAbrigo(abrigo, quantidadePessoas)) {
            System.out.println("O abrigo " + abrigo.getNome() + " não comporta mais " + quantidadePessoas + " pessoas.");
            List<Abrigo> abrigosComVaga = abrigoService.buscarAbrigosComVaga(quantidadePessoas, abrigo.getBairro());

            if (abrigosComVaga.isEmpty()) {
                System.out.println("Nenhum abrigo com vaga. A família será acolhida em superlotação.");
            } else {
                System.out.println("Abrigos com vaga (mesmo bairro primeiro):");
                for (Abrigo comVaga : abrigosComVaga) {
                    System.out.println("ID " + comVaga.getId() + " - " + comVaga.getNome() + " (" + comVaga.getBairro() + ")");
                }
                Long idEscolhido = lerLong("ID do abrigo para encaminhar (0 para manter neste abrigo): ");
                for (Abrigo comVaga : abrigosComVaga) {
                    if (comVaga.getId().equals(idEscolhido)) {
                        abrigoDestino = comVaga;
                        encaminhada = true;
                    }
                }
                if (!encaminhada) {
                    System.out.println("A família será mantida em " + abrigo.getNome() + " (superlotação).");
                }
            }
        }

        Acolhimento acolhimento = acolhimentoService.registrarChegada(familia, abrigoDestino, evento, quantidadePessoas, encaminhada);
        System.out.println("Chegada registrada com sucesso!");
        System.out.println(acolhimento);

        if (acolhimentoService.atingiuAlerta(abrigoDestino)) {
            System.out.println("ALERTA: o abrigo " + abrigoDestino.getNome() + " atingiu o limite de lotação.");
        }
    }

    private static void completarCadastro(FamiliaService familiaService) {
        Familia familia = familiaService.buscarFamiliaPorId(lerLong("ID da família: "));
        if (familia == null) {
            System.out.println("Família não encontrada.");
            return;
        }
        System.out.println(familia);
        while (lerSimNao("Deseja adicionar um membro? (S/N): ")) {
            String nome = lerTexto("Nome do membro: ");
            LocalDate dataNascimento = lerDataOpcional("Data de nascimento (aaaa-mm-dd, Enter se não souber): ");
            familiaService.adicionarMembro(familia, new Membro(nome, dataNascimento));
        }
        String cpf = lerTextoOpcional("CPF do responsável (Enter se não tiver): ");
        String telefone = lerTextoOpcional("Telefone (Enter se não tiver): ");
        familiaService.completarCadastro(familia, cpf, telefone);
        System.out.println("Cadastro completado com sucesso!");
    }

    private static void buscarFamilia(FamiliaService familiaService) {
        List<Familia> familias;
        if (lerInteiro("1. Buscar por nome / 2. Buscar por CPF: ", 1, 2) == 1) {
            familias = familiaService.buscarFamiliasPorNome(lerTexto("Nome (ou parte do nome): "));
        } else {
            familias = familiaService.buscarFamiliasPorCpf(lerTexto("CPF: "));
        }
        if (familias.isEmpty()) {
            System.out.println("Nenhuma família encontrada.");
        }
        familias.forEach(System.out::println);
    }

    private static void excluirFamilia(FamiliaService familiaService) {
        Familia familia = familiaService.buscarFamiliaPorId(lerLong("ID da família a ser excluída: "));
        if (familia == null) {
            System.out.println("Família não encontrada.");
            return;
        }
        familiaService.excluir(familia);
        System.out.println("Família excluída com sucesso!");
    }

    private static void registrarSaida(AcolhimentoService acolhimentoService, AbrigoService abrigoService) {
        if (!listarAcolhidosPorAbrigo(acolhimentoService, abrigoService)) {
            return;
        }
        Acolhimento acolhimento = acolhimentoService.buscarAcolhimentoPorId(lerLong("ID do acolhimento: "));
        if (acolhimento == null) {
            System.out.println("Acolhimento não encontrado.");
            return;
        }
        LocalDate dataSaida = lerDataOpcional("Data de saída (aaaa-mm-dd, Enter para hoje): ");
        if (dataSaida == null) {
            dataSaida = LocalDate.now();
        }
        acolhimentoService.registrarSaida(acolhimento, dataSaida);
        System.out.println("Saída registrada com sucesso!");
    }

    private static boolean listarAcolhidosPorAbrigo(AcolhimentoService acolhimentoService, AbrigoService abrigoService) {
        Abrigo abrigo = abrigoService.buscarAbrigoPorId(lerLong("ID do abrigo: "));
        if (abrigo == null) {
            System.out.println("Abrigo não encontrado.");
            return false;
        }
        List<Acolhimento> acolhidos = acolhimentoService.buscarAcolhidosPorAbrigo(abrigo.getId());
        if (acolhidos.isEmpty()) {
            System.out.println("Nenhuma família acolhida neste abrigo.");
            return false;
        }
        acolhidos.forEach(System.out::println);
        return true;
    }

    private static void listarPendentesPorAbrigo(AcolhimentoService acolhimentoService, AbrigoService abrigoService) {
        Abrigo abrigo = abrigoService.buscarAbrigoPorId(lerLong("ID do abrigo: "));
        if (abrigo == null) {
            System.out.println("Abrigo não encontrado.");
            return;
        }
        List<Acolhimento> pendentes = acolhimentoService.buscarPendentesPorAbrigo(abrigo.getId());
        if (pendentes.isEmpty()) {
            System.out.println("Nenhum cadastro pendente neste abrigo.");
        }
        pendentes.forEach(System.out::println);
    }

    private static void relatorioEncaminhamentos(RelatorioService relatorioService) {
        System.out.println("--- Encaminhamentos por abrigo cheio ---");
        relatorioService.relatorioEncaminhamentos().forEach(System.out::println);
        System.out.println("--- Abrigos superlotados ---");
        relatorioService.relatorioSuperlotados().forEach(System.out::println);
    }

    private static void salvarBoletim(RelatorioService relatorioService) {
        relatorioService.salvarBoletim();
        System.out.println("Boletim do dia salvo com sucesso!");
    }

    private static void verBoletim(RelatorioService relatorioService) {
        BoletimVo boletim = relatorioService.carregarBoletim();
        System.out.println(boletim != null ? boletim.toString() : "Nenhum boletim salvo.");
    }

    private static void configurarPercentualAlerta() {
        System.out.println("Percentual atual: " + ConfiguracaoArquivo.lerPercentualAlerta() + "%");
        ConfiguracaoArquivo.salvarPercentualAlerta(lerInteiro("Novo percentual (1 a 100): ", 1, 100));
        System.out.println("Percentual salvo com sucesso!");
    }

    private static String lerTipoEvento() {
        int tipo = lerInteiro("Tipo (1. Enchente / 2. Vendaval / 3. Deslizamento / 4. Outro): ", 1, 4);
        return switch (tipo) {
            case 1 -> "Enchente";
            case 2 -> "Vendaval";
            case 3 -> "Deslizamento";
            default -> "Outro";
        };
    }

    private static int lerInteiro(String mensagem, int minimo, int maximo) {
        int valor = 0;
        boolean valido;
        do {
            System.out.print(mensagem);
            try {
                valor = Integer.parseInt(scanner.nextLine().trim());
                valido = valor >= minimo && valor <= maximo;
            } catch (NumberFormatException e) {
                valido = false;
            }
            if (!valido) {
                System.out.println("Valor inválido.");
            }
        } while (!valido);
        return valor;
    }

    private static Long lerLong(String mensagem) {
        Long valor = null;
        do {
            System.out.print(mensagem);
            try {
                valor = Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido.");
            }
        } while (valor == null);
        return valor;
    }

    private static String lerTexto(String mensagem) {
        String texto;
        do {
            System.out.print(mensagem);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Valor inválido.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    private static String lerTextoOpcional(String mensagem) {
        System.out.print(mensagem);
        String texto = scanner.nextLine().trim();
        return texto.isEmpty() ? null : texto;
    }

    private static LocalDate lerDataOpcional(String mensagem) {
        LocalDate data = null;
        boolean valida;
        do {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                return null;
            }
            try {
                data = LocalDate.parse(entrada, DateTimeFormatter.ISO_LOCAL_DATE);
                valida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida.");
                valida = false;
            }
        } while (!valida);
        return data;
    }

    private static boolean lerSimNao(String mensagem) {
        String resposta;
        do {
            System.out.print(mensagem);
            resposta = scanner.nextLine().trim().toUpperCase();
            if (!resposta.equals("S") && !resposta.equals("N")) {
                System.out.println("Valor inválido.");
            }
        } while (!resposta.equals("S") && !resposta.equals("N"));
        return resposta.equals("S");
    }
}