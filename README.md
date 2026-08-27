# Serviço de notificação por e-mail

Este serviço é responsável exclusivamente por:

1. consultar as aulas e os participantes no banco
2. publicar o evento em RabbitMQ
3. consumir a fila e enviar e-mails para professor e alunos

## Arquitetura

- Domain: modelos, eventos e portas de domínio
- Application: use cases de processamento e envio
- Infrastructure: adapters de banco, RabbitMQ e SMTP

## Requisitos de execução

- Docker
- Java 21+
- MySQL com as tabelas `aula`, `contrato`, `professor`, `turma`, `aluno`, `disponibilidade_turma` e `disponibilidade_aluno`
- RabbitMQ `rabbitmq:3-management`

## Configuração

1. Ajuste o arquivo `.env` com as credenciais do MySQL, RabbitMQ e e-mail.
2. Suba o broker localmente:
   `docker compose up -d rabbitmq`
3. Inicie a aplicação:
   `./mvnw spring-boot:run`

Exemplo de `.env`:

DB_HOST=localhost
DB_PORT=3306
DB_NAME=boost
DB_USERNAME=root
DB_PASSWORD=sua_senha
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_EXCHANGE=escola.notificacao.exchange
RABBITMQ_QUEUE=escola.notificacao.queue
RABBITMQ_ROUTING_KEY=escola.notificacao.routing
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-app
MAIL_SMTP_AUTH=true
MAIL_SMTP_STARTTLS=true

## Estrutura do evento RabbitMQ

{
  "eventId": "UUID",
  "eventType": "NOTIFICAR_AULA",
  "timestamp": "2026-08-27T18:00:00Z",
  "payload": {
    "aulaId": 1,
    "dataAula": "2026-09-01",
    "horario": "08:00 - 09:00",
    "professor": { "nome": "Ricardo", "email": "ricardo@email.com" },
    "alunos": [
      { "nome": "Ana Souza", "email": "ana@email.com" }
    ]
  }
}

## Consulta nativa otimizada

A consulta no repositório usa `GROUP_CONCAT` para consolidar alunos e professores em uma única linha por aula.

