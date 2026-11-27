# Sistema de Gestão de Abrigos Emergenciais em Situações de Desastre

## Objetivo
O sistema apoia as equipes que atuam em abrigos emergenciais (Defesa Civil, assistência social e voluntários) durante desastres como enchentes, vendavais e deslizamentos. Ele registra a chegada das famílias, controla a ocupação de cada abrigo, sugere abrigos com vaga quando um deles está cheio e gera relatórios que ajudam no atendimento e no planejamento da prevenção.

O projeto contribui para o **ODS 11 – Cidades e Comunidades Sustentáveis**, meta **11.5**, que trata da redução do número de pessoas afetadas por desastres, com foco nas pessoas em situação de vulnerabilidade.

## Requisitos
- **Linguagem de Programação:** Java
- **JDK:** 25
- **IDE:** IntelliJ IDEA
- **Maven:** gerenciamento de dependências e build
- **Persistência:** JPA com Hibernate
- **Banco de Dados Relacional:** PostgreSQL

## Sobre o Projeto
Aplicação Java desktop, com interação por linha de comando. O sistema é operado pela equipe do abrigo, e não pelas famílias atendidas.

### Funcionalidades

**Eventos**
1. Cadastrar Evento
2. Encerrar Evento
3. Excluir Evento
4. Listar Eventos

**Abrigos**

5. Cadastrar Abrigo
6. Alterar Abrigo
7. Excluir Abrigo
8. Listar Abrigos com ocupação

**Famílias e Acolhimentos**

9. Registrar chegada
10. Completar cadastro da família
11. Buscar família por nome ou CPF
12. Excluir família
13. Registrar saída
14. Listar acolhidos por abrigo
15. Listar cadastros pendentes por abrigo

**Relatórios**

16. Ocupação dos abrigos
17. Atendimento por evento
18. Bairros mais atingidos
19. Encaminhamentos e superlotação

**Arquivos**

20. Salvar boletim do dia
21. Ver boletim salvo
22. Configurar percentual de alerta de lotação

## Fluxo de Atendimento
1. A família chega ao abrigo e a equipe faz um pré-cadastro rápido: nome do responsável, bairro de origem e quantidade de pessoas.
2. O sistema verifica a ocupação do abrigo. Se houver vaga, o acolhimento é registrado ali. Se não houver, o sistema lista os abrigos com vaga, mostrando primeiro os do mesmo bairro, e a equipe escolhe para onde encaminhar a família ou a mantém no abrigo em superlotação.
3. Depois, com calma, a equipe completa o cadastro da família com os membros, o CPF e o telefone.
4. Na saída, a equipe registra a data de saída e encerra o acolhimento.

## Regras de Negócio
- Nenhuma família fica sem abrigo: se não houver vaga, o acolhimento é registrado em superlotação.
- Quando a ocupação atinge o percentual definido no arquivo de configuração, o sistema exibe um alerta de lotação.
- Uma família só pode ter um acolhimento aberto por vez.
- A data de saída não pode ser anterior à data de entrada, e a data final de um evento não pode ser anterior à data inicial.
- A ocupação é calculada pela quantidade de pessoas informada na chegada, pois os membros podem ainda não estar cadastrados.
- O cadastro só pode ser completado com pelo menos um membro, e o CPF não pode se repetir entre famílias.
- O CPF e o telefone são opcionais, já que muitas pessoas perdem os documentos em um desastre.
- Eventos, abrigos e famílias com acolhimentos registrados não podem ser excluídos.

## Modelo de Dados
- **Evento:** nome, tipo, data inicial e data final.
- **Abrigo:** nome, endereço, bairro e capacidade.
- **Familia:** nome do responsável, bairro de origem, CPF, telefone e situação do cadastro. Possui uma lista de membros.
- **Membro:** nome e data de nascimento. Pertence a uma família.
- **Acolhimento:** data de entrada, data de saída, quantidade de pessoas e indicação de encaminhamento. Liga uma família a um abrigo e a um evento.

## Conceitos Aplicados
- **Programação Orientada a Objetos:** encapsulamento, herança e generics (DAO genérico), classe abstrata e exceção personalizada.
- **JPA e Mapeamento Objeto-Relacional:** entidades com relacionamentos `@ManyToOne` e `@OneToMany`, consultas JPQL e `SELECT NEW`.
- **Padrões DAO e VO:** acesso a dados isolado e objetos próprios para os relatórios.
- **Manipulação de arquivos:** arquivo de configuração com o percentual de alerta de lotação.
- **Serialização de objetos:** o relatório de ocupação é salvo como boletim do dia em arquivo binário e recuperado depois.
- **API Streams:** cálculo da ocupação, sugestão de abrigos com vaga e relatórios.

## Estrutura do Projeto
- **model:** classes de entidade.
- **dao:** classes de acesso a dados, incluindo o DAO genérico.
- **service:** classes com a lógica e as regras de negócio.
- **vo:** classes que representam os relatórios.
- **arquivo:** leitura e gravação do arquivo de configuração e serialização do boletim.
- **exception:** exceção personalizada para erros de acesso a dados.
- **util:** classe utilitária para JPA.

## Como Executar
1. Criar no PostgreSQL o banco de dados `marci_goncalves`. Apenas o banco: as tabelas são criadas automaticamente pela aplicação.
2. Ajustar o usuário e a senha no arquivo `src/main/resources/META-INF/persistence.xml`, se forem diferentes.
3. Atualizar as dependências do Maven.
4. Executar a classe `Main`.

Os arquivos `configuracao.txt` e `boletim.bin` são criados na pasta do projeto na primeira vez em que forem usados.
