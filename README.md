# backend-mensageria

Microsservico que consome uma fila RabbitMQ e envia um aviso de vencimento de
contrato por WhatsApp. O aviso e enviado para **duas pessoas**: o `destinatario`
(administrador do sistema) e o `aluno`.

```
RabbitMQ  ->  Listener  ->  Use Case  ->  Porta  ->  Gateway  ->  Twilio SDK
```

## Sumario

- [Arquitetura](#arquitetura)
- [Estrutura de pacotes](#estrutura-de-pacotes)
- [Regra de dependencia](#regra-de-dependencia)
- [Trocando o provedor de WhatsApp](#trocando-o-provedor-de-whatsapp)
- [Payload da mensagem](#payload-da-mensagem)
- [Tratamento de erros](#tratamento-de-erros)
- [Pre-requisitos](#pre-requisitos)
- [Configuracao](#configuracao)
- [Como executar](#como-executar)
- [Como testar](#como-testar)

## Arquitetura

O projeto segue Clean Architecture. As regras de negocio nao dependem de Spring,
RabbitMQ nem Twilio: elas descrevem *o que* deve acontecer, enquanto os adapters
descrevem *como* a mensagem entra e sai do sistema.

| Camada | Pacote | Responsabilidade | Depende de |
| --- | --- | --- | --- |
| Domain / Entities | `domain` | Regras de negocio e objetos de dominio | Nada |
| Use Cases | `usecase` | Orquestra o fluxo e chama as portas | `domain` |
| Interface Adapters | `adapters` | Listener (entrada) e gateway (saida) | `domain`, `usecase`, `infrastructure` |
| Frameworks & Drivers | `infrastructure` | Spring Boot, Spring AMQP, SDK do Twilio | `domain` |

Fluxo de uma mensagem:

1. `AvisoContratoRabbitListener` recebe o JSON da fila e converte para o dominio.
2. `EnviarAvisosWhatsAppUseCaseImpl` aplica a regra de negocio e itera sobre
   `aviso.mensagens()`, que devolve os destinatarios na ordem: admin e depois aluno.
3. `WhatsAppServicePort` e chamado uma vez por destinatario, com telefone e texto
   ja formatados.
4. `TwilioWhatsAppServiceAdapter` traduz para o formato do Twilio.
5. `TwilioWhatsAppClient` executa a chamada HTTP usando o SDK oficial.

## Estrutura de pacotes

```
src/main/java/com/escola/mensageria/
├── MensageriaApplication.java              # Frameworks & Drivers: bootstrap
│
├── domain/                                 # Enterprise Business Rules (Java puro)
│   ├── entity/
│   │   ├── AvisoContrato.java              #   agregado + regra que formata os textos
│   │   ├── Aluno.java                      #   nome + telefone
│   │   ├── Destinatario.java               #   nome + telefone (admin do sistema)
│   │   ├── Contrato.java                   #   id, tipo, dataInicio, dataFim
│   │   ├── Turma.java                      #   id, nome, nivel, limiteAlunos, ...
│   │   └── Professor.java                  #   pode ser nulo no payload
│   ├── valueobject/
│   │   └── Telefone.java                   #   normalizacao para E.164 e "whatsapp:"
│   ├── gateway/
│   │   └── WhatsAppServicePort.java        #   porta de saida (interface)
│   ├── exception/
│   │   ├── MensageriaException.java
│   │   ├── DadosInvalidosException.java
│   │   ├── TelefoneInvalidoException.java
│   │   └── FalhaEnvioWhatsAppException.java
│   └── support/
│       └── Precondicoes.java
│
├── usecase/                                # Application Business Rules
│   ├── EnviarAvisosWhatsAppUseCase.java
│   └── impl/
│       └── EnviarAvisosWhatsAppUseCaseImpl.java
│
├── adapters/                               # Interface Adapters
│   ├── inbound/
│   │   ├── AvisoContratoRabbitListener.java  # @RabbitListener
│   │   └── dto/AvisoContratoRequest.java    # mapeamento do JSON
│   └── outbound/
│       └── TwilioWhatsAppServiceAdapter.java
│
└── infrastructure/                         # Frameworks & Drivers
    ├── client/
    │   └── TwilioWhatsAppClient.java        # unica classe que importa com.twilio.*
    ├── config/
    │   ├── RabbitMQConfig.java
    │   ├── RabbitMensageriaProperties.java
    │   ├── WhatsAppConfig.java
    │   ├── WhatsAppProperties.java
    │   └── UseCaseConfig.java              # bean do use case
    └── exception/
        └── WhatsAppClientException.java
```

### Observacoes de modelagem

- `Professor` e `Turma` sao **opcionais**: o payload pode mandar `professor: null`,
  e as mensagens apenas omitem a linha correspondente.
- `Professor` **nao tem telefone** no dominio. Como o professor nao recebe
  mensagem, exigir um telefone em E.164 valido rejeitaria payloads legitimos sem
  necessidade.
- O dominio nao repete o payload: os campos que nao influenciam a mensagem
  (`email`, `ativo`, `publicadoEm`, `limiteAlunos`) ficam disponiveis, mas nao
  sao usados na regra.

## Regra de dependencia

As dependencias apontam sempre para dentro:

```
infrastructure  ->  adapters  ->  usecase  ->  domain
```

O pacote `domain` nao importa nada de Spring, RabbitMQ ou Twilio. Isso e
verificavel por inspecao: nenhum arquivo em `domain/` referencia `org.springframework`
ou `com.twilio`.

Excecao consciousa: `EnviarMensagemWhatsAppUseCaseImpl` usa SLF4J para logging.
SLF4J e apenas uma fachada (sem implementacao no classpath), e o logs e requisito
operacional. Se voce quiser a camada de aplicacao 100% livre de qualquer dependencia
externa, crie uma porta `NotificadorPort` no dominio e injete-a.

## Trocando o provedor de WhatsApp

O caso de uso conhece apenas a interface `WhatsAppServicePort`. Para migrar do Twilio
para outro provedor (Meta Cloud API, Zenvia, AWS SNS):

1. Crie `adapters/outbound/MetaWhatsAppServiceAdapter implements WhatsAppServicePort`.
2. Crie `infrastructure/client/MetaWhatsAppClient` para o SDK da Meta.
3. Atualize a anotacao `@Component` ou o bean em `infrastructure/config/UseCaseConfig`.

Nada em `domain/`, `usecase/` ou no listener precisa ser alterado.

## Payload da mensagem

```json
{
  "contrato": { "id": 12, "tipo": "Grupo", "dataInicio": "2026-08-01", "dataFim": "2026-10-20" },
  "aluno": { "id": 1, "nome": "Guilherme Silva2", "email": "guilherme.silva@sptech.school", "telefone": "11977776666", "nível": "Intermediário", "ativo": true },
  "professor": null,
  "turma": { "id": 1, "nome": "ADS-2B", "nível": "Intermediário", "limiteAlunos": 30, "tipo": "Grupo", "professorId": 1 },
  "destinatario": { "id": 1, "nome": "Administrador Sistema", "email": "admin@sptech.school", "telefone": "1199999888", "ativo": true },
  "diasParaVencer": 18,
  "dataAviso": "2026-10-02",
  "publicadoEm": "2026-10-02T10:15:30"
}
```

Campos desconhecidos sao ignorados. `professor` e `turma` podem ser `null`.
`dataAviso` e `publicadoEm` sao lidos como data local e data/hora local.

Os telefones podem vir em qualquer formato aceito pelo value object `Telefone`
(`11977776666`, `(11) 9999-8888`, `+55 11 99999-9999`, `01199999999`). Sem prefixo
`+` ou `00`, o codigo do Brasil (`55`) e aplicado automaticamente para numeros de
10 ou 11 digitos.

### Mensagens geradas

Para o `destinatario` (administrador):

```
Olá, Administrador Sistema!

O contrato nº 12 do aluno Guilherme Silva2 vence em 20/10/2026 (faltam 18 dias).
Turma: ADS-2B.

Solicitamos a regularização da renovação antes da data de vencimento.

Atenciosamente,
Equipe Boost
```

Para o `aluno`:

```
Olá, Guilherme Silva2!

Informamos que o seu contrato da turma ADS-2B vence em 20/10/2026 (faltam 18 dias).

Renove sua matrícula para não perder sua vaga.

Atenciosamente,
Equipe Boost
```

Se `professor` vier preenchido, a mensagem do administrador ganha a linha
`Professor responsável: <nome>.`

## Tratamento de erros

- Entrada invalida (nome vazio, telefone malformado): `DadosInvalidosException`
  ou `TelefoneInvalidoException`.
- Falha do Twilio: `ApiException` do SDK vira `WhatsAppClientException`
  (infraestrutura) e depois `FalhaEnvioWhatsAppException` (dominio). O caso de uso
  nao conhece a excecao do SDK.
- Os envios sao **fail-fast**: se o envio para o administrador falhar, o do aluno
  nao e tentado e a excecao sobe para o listener. Como a entrega e *at-least-once*,
  uma retentativa pode reenviar a mensagem do administrador. Se preferir enviar os
  dois mesmo assim, acumule as falhas em `EnviarAvisosWhatsAppUseCaseImpl` e lance
  ao final do laco.
- A fila principal tem dead letter: apos 3 tentativas com backoff, a mensagem vai
  para `mensageria.aula.dlq`.

## Pre-requisitos

- JDK 21+ (o projeto usa records, text blocks e value objects imutaveis)
- Docker (para o RabbitMQ)
- Conta no Twilio com um numero ou Messaging Service de WhatsApp habilitado

## Configuracao

Copie `.env.example` para `.env` e preencha:

```bash
cp .env.example .env
```

| Variavel | Obrigatoria | Descricao |
| --- | --- | --- |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | nao | Padrao `localhost:5672` |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | nao | Padrao `guest`/`guest` |
| `RABBITMQ_EXCHANGE` / `RABBITMQ_QUEUE` / `RABBITMQ_ROUTING_KEY` | nao | Nomes da topologia |
| `TWILIO_ACCOUNT_SID` | **sim** | Account SID do Twilio |
| `TWILIO_AUTH_TOKEN` | **sim** | Auth token do Twilio |
| `WHATSAPP_MESSAGING_SERVICE_SID` | uma das duas | Identifica o remetente (recomendado pelo Twilio) |
| `WHATSAPP_REMETENTE` | uma das duas | Numero remetente, ex.: `+14155238886` |
| `WHATSAPP_CONTENT_SID` | nao | Content Template aprovado pelo Twilio |

O arquivo `.env` esta no `.gitignore` e nunca deve ser versionado.

**Sobre templates do WhatsApp:** fora da janela de 24 horas de conversa, o Twilio
exige um Content Template aprovado. Configure `WHATSAPP_CONTENT_SID` para usar um
template; sem ele, o servico envia texto livre e o Twilio pode recusar a mensagem.

## Como executar

### Com Docker Compose (recomendado)

```bash
cp .env.example .env    # preencha as credenciais do Twilio
docker compose up --build
```

O RabbitMQ fica disponivel em `http://localhost:15672` (guest/guest).

### Localmente

```bash
cp .env.example .env
./start.sh
```

O script sobe o RabbitMQ, aguarda ficar saudavel e roda `./mvnw spring-boot:run`.

### Publicar uma mensagem de teste

Com a aplicacao no ar:

```bash
./publicar-exemplo.sh
```

O script publica o payload de exemplo na exchange via API de gerenciamento. Voce
tambem pode usar os argumentos positionalmente:

```bash
./publicar-exemplo.sh "Guilherme Silva2" "11977776666" "Administrador Sistema" "1199999888"
```

## Como testar

```bash
./mvnw test
```

A suite cobre 36 casos, incluindo um teste de integracao com RabbitMQ real via
Testcontainers que publica o payload de exemplo acima na fila:

| Teste | O que verifica |
| --- | --- |
| `TelefoneTest` | Normalizacao para E.164 e rejeicao de entradas invalidas |
| `AvisoContratoTest` | Textos das duas mensagens, professor/turma nulos, singular/plural |
| `AvisoContratoRequestTest` | Conversao do payload para o dominio |
| `EnviarAvisosWhatsAppUseCaseImplTest` | Envio para os dois destinatarios usando uma porta falsa, sem Spring |
| `TwilioWhatsAppServiceAdapterTest` | Conversao de destino e traducao de erros |
| `TwilioWhatsAppClientTest` | Validacao de credenciais e de remetente |
| `AvisoContratoRabbitListenerIntegrationTest` | Fila RabbitMQ real ate a porta, ponta a ponta |

O teste de integracao e pulado automaticamente quando nao ha Docker disponivel
(`@Testcontainers(disabledWithoutDocker = true)`).

Para rodar apenas os testes unitarios, sem Docker:

```bash
./mvnw test -Dtest='!AvisoContratoRabbitListenerIntegrationTest'
```

> **Nota:** esta maquina tem apenas o JRE instalado (sem `javac`). Instale um JDK
> 21+ para compilar localmente, ou use `docker compose up --build`.
