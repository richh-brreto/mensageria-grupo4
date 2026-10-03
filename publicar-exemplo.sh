#!/usr/bin/env bash
set -euo pipefail

RABBITMQ_HOST="${RABBITMQ_HOST:-localhost}"
RABBITMQ_PORT="${RABBITMQ_PORT:-15672}"
RABBITMQ_USERNAME="${RABBITMQ_USERNAME:-guest}"
RABBITMQ_PASSWORD="${RABBITMQ_PASSWORD:-guest}"
RABBITMQ_EXCHANGE="${RABBITMQ_EXCHANGE:-mensageria.aula.exchange}"
RABBITMQ_ROUTING_KEY="${RABBITMQ_ROUTING_KEY:-mensageria.aula.routing}"

NOME_ALUNO="${1:-Guilherme Silva2}"
TELEFONE_ALUNO="${2:-11977776666}"
NOME_DESTINATARIO="${3:-Administrador Sistema}"
TELEFONE_DESTINATARIO="${4:-1199999888}"

PAYLOAD=$(cat <<JSON
{
  "contrato": {
    "id": 12,
    "tipo": "Grupo",
    "dataInicio": "2026-08-01",
    "dataFim": "2026-10-20"
  },
  "aluno": {
    "id": 1,
    "nome": "$NOME_ALUNO",
    "email": "guilherme.silva@sptech.school",
    "telefone": "$TELEFONE_ALUNO",
    "nível": "Intermediário",
    "ativo": true
  },
  "professor": null,
  "turma": {
    "id": 1,
    "nome": "ADS-2B",
    "nível": "Intermediário",
    "limiteAlunos": 30,
    "tipo": "Grupo",
    "professorId": 1
  },
  "destinatario": {
    "id": 1,
    "nome": "$NOME_DESTINATARIO",
    "email": "admin@sptech.school",
    "telefone": "$TELEFONE_DESTINATARIO",
    "ativo": true
  },
  "diasParaVencer": 18,
  "dataAviso": "2026-10-02",
  "publicadoEm": "2026-10-02T10:15:30"
}
JSON
)

echo "Publicando na fila ${RABBITMQ_EXCHANGE} (routing key ${RABBITMQ_ROUTING_KEY})..."

curl -fsS -u "${RABBITMQ_USERNAME}:${RABBITMQ_PASSWORD}" \
  -H "content-type:application/json" \
  -X POST "http://${RABBITMQ_HOST}:${RABBITMQ_PORT}/api/exchanges/%2F/${RABBITMQ_EXCHANGE}/publish" \
  -d "{\"properties\":{\"content_type\":\"application/json\"},\"routing_key\":\"${RABBITMQ_ROUTING_KEY}\",\"payload\":$(printf '%s' "$PAYLOAD" | jq -Rs .),\"payload_encoding\":\"string\"}"

echo
echo "Mensagem publicada. Acompanhe o log da aplicacao."
