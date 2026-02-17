Curso: Bootcamp Quality Assurance
Discente: Diogo Mascarenhas Ferreira Santos

Processo de Testes - Técnicas de Testes

Instruções:

Elabore um documento contendo a definição e pelo menos dois exemplos de cada uma das técnicas abaixo:

Análise de Valor Limite
Tabela de Decisão
Fluxo de Controle (Caixa Branca)

Técnicas de Teste de Software - Aplicação de Gerenciamento de Eventos

ANÁLISE DE VALOR LIMITE (BOUNDARY TESTING)

Definição

A Análise de Valor Limite é uma técnica de teste que foca em testar os limites ou fronteiras dos domínios de entrada. Uma vez que bugs tendem a ocorrer nas chamadas bordas das partições de entrada, nos pontos "on" (dentro do limite) e "off" (fora do limite). Esta técnica é eficaz porque muitos erros ocorrem justamente nestes pontos de transição entre comportamentos válidos e inválidos.
Uma forma prática é exercitar “on point” (no limite) e “off point” (o valor mais próximo que muda o resultado), e opcionalmente usar “in” (um valor válido dentro da partição (opcional, mas útil para legibilidade) “out points” (um valor inválido fora da partição (opcional) para dar mais contexto.

Exemplo 1: Número Máximo de Participantes

Contexto: Um evento pode ter um número máximo de participantes (por exemplo, 100). O sistema deve aceitar confirmações até este limite.

Partições identificadas:

Participantes abaixo do limite: válido
Participantes no limite exato: válido
Participantes acima do limite: inválido

Casos de teste:

```java
@Test
void deveAceitarConfirmacaoQuandoAbaixoDoLimite() {
    // Arrange
    Evento evento = new Evento("Festa de Casamento", 100, LocalDateTime.now().plusDays(80)); // limite: 100
    preencherParticipantes(evento, 98); // 98 confirmados

    // Act
    boolean resultado = evento.confirmarParticipante(
        new Participante("João Silva", "Levarei sobremesa")
    );

    // Assert
    assertThat(resultado).isTrue();
    assertThat(evento.getNumeroConfirmados()).isEqualTo(99); // on-point: 99
}

@Test
void deveAceitarConfirmacaoNoLimiteExato() {
    // Arrange
    Evento evento = new Evento("Festa de Casamento", 100, LocalDateTime.now().plusDays(80));
    preencherParticipantes(evento, 99); // 99 confirmados

    // Act
    boolean resultado = evento.confirmarParticipante(
        new Participante("Maria Santos", null)
    );

    // Assert
    assertThat(resultado).isTrue();
    assertThat(evento.getNumeroConfirmados()).isEqualTo(100); // on-point: 100 (limite exato)
}

@Test
void deveRecusarConfirmacaoAcimaDoLimite() {
    // Arrange
    Evento evento = new Evento("Festa de Casamento", 100, LocalDateTime.now().plusDays(80));
    preencherParticipantes(evento, 100); // 100 confirmados (lotado)

    // Act & Assert
    assertThatThrownBy(() -> 
        evento.confirmarParticipante(new Participante("Pedro Costa", null))
    ).isInstanceOf(EventoLotadoException.class)
     .hasMessage("Evento já atingiu o número máximo de participantes"); // off-point: 101
}

@Test
void deveAceitarConfirmacaoQuandoEventoIlimitado() {
    // Arrange
    Evento evento = new Evento("Corrida Solidária", null, LocalDateTime.now().plusDays(40)); // sem limite
    preencherParticipantes(evento, 200);

    // Act
    boolean resultado = evento.confirmarParticipante(
        new Participante("Ana Paula", "Confirmo presença!")
    );

    // Assert
    assertThat(resultado).isTrue();
    assertThat(evento.getNumeroConfirmados()).isEqualTo(201);
}
```

Exemplo 2: Validação de Data do Evento

Contexto: Um evento deve ter uma data futura. O sistema não pode aceitar eventos no passado.

Partições identificadas:

Data no passado: inválido
Data hoje: limite
Data no futuro: válido

Casos de teste:

```java
@Test
void deveRejeitarEventoComDataNoPassado() {
    // Arrange
    LocalDateTime dataPassada = LocalDateTime.now().minusDays(1); // off-point: ontem

    // Act & Assert
    assertThatThrownBy(() -> 
        new Evento("Reunião", 50, dataPassada)
    ).isInstanceOf(IllegalArgumentException.class)
     .hasMessage("A data do evento não pode ser no passado");
}

@Test
void deveAceitarEventoComDataHoje() {
    // Arrange
    LocalDateTime hoje = LocalDateTime.now(); // on-point: hoje (limite inferior)

    // Act
    Evento evento = new Evento("Workshop", 30, hoje);

    // Assert
    assertThat(evento.getData()).isEqualTo(hoje);
}

@Test
void deveAceitarEventoComDataFutura() {
    // Arrange
    LocalDateTime amanha = LocalDateTime.now().plusDays(1); // on-point: amanhã

    // Act
    Evento evento = new Evento("Palestra", 100, amanha);

    // Assert
    assertThat(evento.getData()).isEqualTo(amanha);
}

@Test
void deveAceitarEventoComDataMuitoFutura() {
    // Arrange
    LocalDateTime umAnoDepois = LocalDateTime.now().plusYears(1); // in-point: bem dentro

    // Act
    Evento evento = new Evento("Conferência Anual", 200, umAnoDepois);

    // Assert
    assertThat(evento.getData()).isEqualTo(umAnoDepois);
}
```

TABELA DE DECISÃO (DECISION TABLE)

Definição

A Tabela de Decisão é uma técnica que ajuda a mapear combinações de condições (entradas) e suas respectivas ações (saídas). É especialmente útil quando o comportamento do sistema depende de múltiplas condições que podem ser verdadeiras ou falsas simultaneamente. A técnica permite identificar sistematicamente todas as combinações relevantes e garantir que cada uma seja testada.

Exemplo 1: Validação de Convite por Link

Contexto: Quando um participante acessa o link do convite, o sistema valida se o token é válido e se o evento ainda permite confirmações.

Condições:

Token é válido?
Token não expirou?
Evento ainda não ocorreu?
Evento tem vagas disponíveis?

Tabela de Decisão:

Regra | R1 | R2 | R3 | R4 | R5 | R6 | R7 | R8
----- |----|----|----|----|----|----|----|----
Token válido        | V | V | V | V | F | F | F | F
Token não expirado  | V | V | F | F | - | - | - | -
Evento futuro       | V | F | - | - | - | - | - | -
Tem vagas           | V | F | - | - | - | - | - | -
Ação: Permitir confirmação      | X |   |   |   |   |   |   |  
Ação: Erro: evento lotado       |   | X |   |   |   |   |   |  
Ação: Erro: token expirado      |   |   | X | X |   |   |   |  
Ação: Erro: token inválido      |   |   |   |   | X | X | X | X

Casos de teste:

```java
@ParameterizedTest
@MethodSource("cenariosDeValidacaoConvite")
void deveValidarConviteSegundoRegrasDefinidas(
    boolean tokenValido,
    boolean tokenExpirado,
    boolean eventoFuturo,
    boolean temVagas,
    ResultadoEsperado esperado
) {
    // Arrange
    String token = tokenValido ? gerarTokenValido() : "token-invalido";
    Evento evento = criarEvento(eventoFuturo, temVagas);
    Convite convite = new Convite(evento, token, 
        tokenExpirado ? LocalDateTime.now().minusHours(25) : LocalDateTime.now());

    ValidadorConvite validador = new ValidadorConvite();

    // Act
    ResultadoValidacao resultado = validador.validar(convite);

    // Assert
    assertThat(resultado.isValido()).isEqualTo(esperado.devePermitir);
    if (!esperado.devePermitir) {
        assertThat(resultado.getMensagemErro()).isEqualTo(esperado.mensagem);
    }
}

static Stream<Arguments> cenariosDeValidacaoConvite() {
    return Stream.of(
        // R1: Tudo válido - permite confirmação
        Arguments.of(true, false, true, true, 
            new ResultadoEsperado(true, null)),

        // R2: Token válido mas evento lotado
        Arguments.of(true, false, true, false, 
            new ResultadoEsperado(false, "Evento já atingiu o número máximo de participantes")),

        // R3: Token válido mas expirado, evento futuro
        Arguments.of(true, true, true, true, 
            new ResultadoEsperado(false, "O token do convite expirou")),

        // R4: Token válido mas expirado, evento passado
        Arguments.of(true, true, false, true, 
            new ResultadoEsperado(false, "O token do convite expirou")),

        // R5-R8: Token inválido (não importam outras condições)
        Arguments.of(false, false, true, true, 
            new ResultadoEsperado(false, "Token de convite inválido")),
        Arguments.of(false, true, true, true, 
            new ResultadoEsperado(false, "Token de convite inválido"))
    );
}
```

Exemplo 2: Permissões de Edição do Evento

Contexto: Um evento pode ser editado dependendo de quem está tentando editar e do estado atual do evento.

Condições:

Usuário é o organizador?
Evento já ocorreu?
Tem participantes confirmados?

Tabela de Decisão:

Regra | R1 | R2 | R3 | R4 | R5 | R6 | R7 | R8
----- |----|----|----|----|----|----|----|----
É organizador      | V | V | V | V | F | F | F | F
Evento futuro      | V | V | F | F | V | V | F | F
Sem confirmados    | V | F | V | F | V | F | V | F
Ação: Edição completa       | X |   |   |   |   |   |   |  
Ação: Edição limitada       |   | X |   |   |   |   |   |  
Ação: Bloqueado: Já ocorreu |   |   | X | X |   |   |   |  
Ação: Sem permissão         |   |   |   |   | X | X | X | X

Casos de teste:

```java
@ParameterizedTest
@MethodSource("cenariosDeEdicaoEvento")
void devePermitirEdicaoConformeRegras(
    boolean ehOrganizador,
    boolean eventoFuturo,
    boolean temConfirmados,
    TipoPermissao permissaoEsperada
) {
    // Arrange
    Usuario usuario = ehOrganizador ? 
        criarOrganizador() : criarUsuarioComum();

    Evento evento = criarEvento(eventoFuturo);
    if (temConfirmados) {
        evento.confirmarParticipante(new Participante("Teste", null));
    }

    ServicoEvento servico = new ServicoEvento();

    // Act
    PermissaoEdicao permissao = servico.verificarPermissaoEdicao(usuario, evento);

    // Assert
    assertThat(permissao.getTipo()).isEqualTo(permissaoEsperada);
}

static Stream<Arguments> cenariosDeEdicaoEvento() {
    return Stream.of(
        // R1: Organizador, futuro, sem confirmados - edição completa
        Arguments.of(true, true, false, TipoPermissao.COMPLETA),

        // R2: Organizador, futuro, com confirmados - edição limitada
        Arguments.of(true, true, true, TipoPermissao.LIMITADA),

        // R3: Organizador, passado, sem confirmados - bloqueado
        Arguments.of(true, false, false, TipoPermissao.BLOQUEADA),

        // R4: Organizador, passado, com confirmados - bloqueado
        Arguments.of(true, false, true, TipoPermissao.BLOQUEADA),

        // R5-R8: Não organizador - sem permissão
        Arguments.of(false, true, false, TipoPermissao.NEGADA),
        Arguments.of(false, true, true, TipoPermissao.NEGADA),
        Arguments.of(false, false, false, TipoPermissao.NEGADA),
        Arguments.of(false, false, true, TipoPermissao.NEGADA)
    );
}
```

FLUXO DE CONTROLE / TESTES ESTRUTURAIS (CAIXA BRANCA)

Definição

O teste de Fluxo de Controle, também conhecido como teste estrutural ou caixa branca, usa o código-fonte como base para criar casos de teste. O objetivo é exercitar diferentes caminhos de execução, branches (ramificações) e condições do código. A cobertura de código (code coverage) é uma métrica importante: line coverage (linhas executadas), branch coverage (todas as ramificações testadas), e MCDC - Modified Condition/Decision Coverage (cada condição afeta independentemente o resultado).

Critérios de Cobertura:

- Cobertura de Linha (Line Coverage): Cada linha executável foi executada pelo menos uma vez;
- Cobertura de Branch (Branch Coverage): Cada decisão (if, while, for) foi avaliada como verdadeira e falsa;
- Cobertura de Condição (Condition Coverage): Cada condição booleana foi avaliada como verdadeira e falsa; e
- MCDC (Modified Condition/Decision Coverage): Cada condição afeta independentemente o resultado da decisão.

Exemplo 1: Método de Processamento de Resposta ao Convite

Contexto: Quando um participante responde ao convite, o sistema precisa processar a resposta e atualizar contadores.

```java
public class ProcessadorResposta {

    public ResultadoProcessamento processarResposta(
        Convite convite, 
        RespostaConvite resposta
    ) {
        // Validação de entrada
        if (convite == null || resposta == null) {
            throw new IllegalArgumentException("Convite e resposta não podem ser nulos");
        }

        // Verifica se convite já foi respondido
        if (convite.foiRespondido()) {
            return new ResultadoProcessamento(false, "Convite já foi respondido anteriormente");
        }

        // Processa aceitação
        if (resposta.isAceitou()) {
            Evento evento = convite.getEvento();

            // Verifica se ainda há vagas
            if (evento.temLimite() && evento.estaLotado()) {
                convite.marcarComoRecusado("Evento lotado");
                return new ResultadoProcessamento(false, "Evento já está lotado");
            }

            // Confirma participação
            Participante participante = new Participante(
                resposta.getNome(),
                resposta.getObservacao()
            );

            evento.adicionarParticipante(participante);
            convite.marcarComoAceito(participante);

            return new ResultadoProcessamento(true, "Participação confirmada com sucesso");
        } 
        // Processa recusa
        else {
            convite.marcarComoRecusado(resposta.getMotivoRecusa());
            return new ResultadoProcessamento(true, "Recusa registrada");
        }
    }
}
```

Análise de Cobertura:

Branches identificados:

1. convite == null || resposta == null (true/false)
2. convite.foiRespondido() (true/false)
3. resposta.isAceitou() (true/false)
4. evento.temLimite() && evento.estaLotado() (true/false quando isAceitou = true)

Casos de teste para 100% branch coverage:

```java
class ProcessadorRespostaTest {

    @Test
    void deveLancarExcecaoQuandoConviteNulo() {
        // Branch 1 (true): convite == null
        ProcessadorResposta processador = new ProcessadorResposta();
        RespostaConvite resposta = new RespostaConvite(true, "João", null);

        assertThatThrownBy(() -> processador.processarResposta(null, resposta))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Convite e resposta não podem ser nulos");
    }

    @Test
    void deveLancarExcecaoQuandoRespostaNula() {
        // Branch 1 (true): resposta == null
        ProcessadorResposta processador = new ProcessadorResposta();
        Convite convite = criarConviteValido();

        assertThatThrownBy(() -> processador.processarResposta(convite, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deveRejeitarQuandoConviteJaRespondido() {
        // Branch 1 (false): entradas válidas
        // Branch 2 (true): convite.foiRespondido() = true
        ProcessadorResposta processador = new ProcessadorResposta();
        Convite convite = criarConviteJaRespondido();
        RespostaConvite resposta = new RespostaConvite(true, "Maria", null);

        ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

        assertThat(resultado.isSucesso()).isFalse();
        assertThat(resultado.getMensagem()).isEqualTo("Convite já foi respondido anteriormente");
    }

    @Test
    void deveAceitarParticipacaoQuandoEventoTemVagas() {
        // Branch 1 (false): entradas válidas
        // Branch 2 (false): convite não foi respondido
        // Branch 3 (true): resposta aceita
        // Branch 4 (false): evento não está lotado
        ProcessadorResposta processador = new ProcessadorResposta();
        Convite convite = criarConviteNaoRespondido();
        RespostaConvite resposta = new RespostaConvite(true, "Carlos", "Levarei refrigerante");

        ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

        assertThat(resultado.isSucesso()).isTrue();
        assertThat(resultado.getMensagem()).isEqualTo("Participação confirmada com sucesso");
        assertThat(convite.foiRespondido()).isTrue();
        assertThat(convite.foiAceito()).isTrue();
    }

    @Test
    void deveRecusarQuandoEventoLotado() {
        // Branch 1 (false): entradas válidas
        // Branch 2 (false): convite não foi respondido
        // Branch 3 (true): resposta aceita
        // Branch 4 (true): evento.temLimite() && evento.estaLotado()
        ProcessadorResposta processador = new ProcessadorResposta();
        Convite convite = criarConviteParaEventoLotado();
        RespostaConvite resposta = new RespostaConvite(true, "Ana", null);

        ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

        assertThat(resultado.isSucesso()).isFalse();
        assertThat(resultado.getMensagem()).isEqualTo("Evento já está lotado");
        assertThat(convite.foiRecusado()).isTrue();
    }

    @Test
    void deveRegistrarRecusaDoParticipante() {
        // Branch 1 (false): entradas válidas
        // Branch 2 (false): convite não foi respondido
        // Branch 3 (false): resposta recusada
        ProcessadorResposta processador = new ProcessadorResposta();
        Convite convite = criarConviteNaoRespondido();
        RespostaConvite resposta = new RespostaConvite(false, null, "Tenho outro compromisso");

        ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

        assertThat(resultado.isSucesso()).isTrue();
        assertThat(resultado.getMensagem()).isEqualTo("Recusa registrada");
        assertThat(convite.foiRecusado()).isTrue();
    }

    @Test
    void deveAceitarParticipacaoQuandoEventoSemLimite() {
        // Branch 1 (false): entradas válidas
        // Branch 2 (false): convite não foi respondido
        // Branch 3 (true): resposta aceita
        // Branch 4 (false): evento sem limite (temLimite() = false)
        ProcessadorResposta processador = new ProcessadorResposta();
        Convite convite = criarConviteParaEventoSemLimite();
        RespostaConvite resposta = new RespostaConvite(true, "Roberto", null);

        ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

        assertThat(resultado.isSucesso()).isTrue();
        assertThat(convite.foiAceito()).isTrue();
    }
}
```

Exemplo 2: Cálculo de Status do Evento

Contexto: O sistema calcula o status de um evento baseado em múltiplas condições.

```java
public class CalculadorStatusEvento {

    public StatusPercentual calcularStatusPercentualConfirmados(Evento evento) {
        // Evento sem limite sempre retorna status especial
        if (!evento.temLimite()) {
            return StatusPercentual.SEM_LIMITE;
        }

        int percentualConfirmados = evento.calcularPercentualConfirmados();

        if (percentualConfirmados >= 100) {
            return StatusPercentual.LOTADO;
        } else if (percentualConfirmados >= 80) {
            return StatusPercentual.QUASE_LOTADO;
        } else if (percentualConfirmados >= 50) {
            return StatusPercentual.BOA_ADESAO;
        } else if (percentualConfirmados > 0) {
            return StatusPercentual.ABERTO_COM_CONFIRMACOES;
        } else {
            return StatusPercentual.ABERTO_SEM_CONFIRMACOES;
        }
    }
}
```

Análise de Branches:

- Branch 1: !evento.temLimite() -> true/false
- Branch 2: percentualConfirmados >= 100 -> true/false
- Branch 3: percentualConfirmados >= 80 -> true/false (quando branch 2 = false)
- Branch 4: percentualConfirmados >= 50 -> true/false (quando branches 2 e 3 = false)
- Branch 5: percentualConfirmados > 0 -> true/false (quando branches 2, 3 e 4 = false)

Casos de teste para branch coverage:

```java
class CalculadorStatusEventoTest {

    @Test
    void deveRetornarSemLimiteQuandoEventoNaoTemLimite() {
        // Branch 1 (true): !evento.temLimite() = true
        Evento evento = criarEventoSemLimite();
        preencherParticipantes(evento, 200);
        CalculadorStatusEvento calculador = new CalculadorStatusEvento();

        StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

        assertThat(status).isEqualTo(StatusPercentual.SEM_LIMITE);
    }

    @Test
    void deveRetornarLotadoQuando100PorCento() {
        // Branch 1 (false): evento tem limite
        // Branch 2 (true): percentual >= 100
        Evento evento = criarEvento(100);
        preencherParticipantes(evento, 100);
        CalculadorStatusEvento calculador = new CalculadorStatusEvento();

        StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

        assertThat(status).isEqualTo(StatusPercentual.LOTADO);
    }

    @Test
    void deveRetornarQuaseLotadoQuando80A99PorCento() {
        // Branch 1 (false): evento tem limite
        // Branch 2 (false): percentual < 100
        // Branch 3 (true): percentual >= 80
        Evento evento = criarEvento(100);
        preencherParticipantes(evento, 85);
        CalculadorStatusEvento calculador = new CalculadorStatusEvento();

        StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

        assertThat(status).isEqualTo(StatusPercentual.QUASE_LOTADO);
    }

    @Test
    void deveRetornarBoaAdesaoQuando50A79PorCento() {
        // Branch 1 (false): evento tem limite
        // Branch 2 (false): percentual < 100
        // Branch 3 (false): percentual < 80
        // Branch 4 (true): percentual >= 50
        Evento evento = criarEvento(100);
        preencherParticipantes(evento, 60);
        CalculadorStatusEvento calculador = new CalculadorStatusEvento();

        StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

        assertThat(status).isEqualTo(StatusPercentual.BOA_ADESAO);
    }

    @Test
    void deveRetornarAbertoComConfirmacoesQuando1A49PorCento() {
        // Branch 1 (false): evento tem limite
        // Branch 2 (false): percentual < 100
        // Branch 3 (false): percentual < 80
        // Branch 4 (false): percentual < 50
        // Branch 5 (true): percentual > 0
        Evento evento = criarEvento(100);
        preencherParticipantes(evento, 20);
        CalculadorStatusEvento calculador = new CalculadorStatusEvento();

        StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

        assertThat(status).isEqualTo(StatusPercentual.ABERTO_COM_CONFIRMACOES);
    }

    @Test
    void deveRetornarAbertoSemConfirmacoesQuandoZeroPorCento() {
        // Branch 1 (false): evento tem limite
        // Branch 2 (false): percentual < 100
        // Branch 3 (false): percentual < 80
        // Branch 4 (false): percentual < 50
        // Branch 5 (false): percentual = 0
        Evento evento = criarEvento(100);
        CalculadorStatusEvento calculador = new CalculadorStatusEvento();

        StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

        assertThat(status).isEqualTo(StatusPercentual.ABERTO_SEM_CONFIRMACOES);
    }
}
```

CONCLUSÃO

As três técnicas exploradas formam um ciclo completo de testes sistemáticos e, embora sejam complementares, cada uma possui um propósito específico e aborda o problema da qualidade de software por ângulos diferentes. Os testes de especificação (Análise de Valor Limite e Tabela de Decisão) costumam ser o ponto de partida, quando os requisitos já estão definidos e precisam ser transformados em casos de teste antes da implementação do código. A análise de valor limite é particularmente eficaz para detectar erros nas fronteiras, como situações em que um operador deveria ser “>” em vez de “>=”, ou limites com erros de um ponto (off-by-one). A tabela de decisão é especialmente adequada em cenários com múltiplas condições interagindo, pois estimula o raciocínio sistemático sobre todas as combinações relevantes e reduz o risco de omissão de casos importantes. 
Os testes estruturais (Fluxo de Controle) entram em cena quando o código já está implementado e existe uma suíte de testes derivada dos requisitos, sendo utilizados como critério ou técnica para projetar testes a partir da estrutura do código, considerando caminhos, branches, condições, laços e métricas como line coverage, branch coverage e MCDC. Nessa abordagem, a preocupação principal é decidir quais caminhos de um método ou classe devem ser exercitados, o que distingue esse tipo de teste de níveis como testes unitários, de integração ou de ponta a ponta, ainda que possam se assemelhar na forma. Nesse contexto, o uso de branch coverage evidencia quais caminhos do código não foram executados pelos testes existentes e ajuda a revelar partições esquecidas ou casos de borda escondidos na implementação.
Mesmo assim, 100% de cobertura não implica ausência de bugs, apenas indica que todas as linhas foram executadas ao menos uma vez. É possível atingir 100% de cobertura e ainda assim não exercitar as condições mais relevantes ou não validar corretamente os resultados. Por esse motivo, o teste estrutural deve sempre complementar, e nunca substituir, os testes de especificação.
