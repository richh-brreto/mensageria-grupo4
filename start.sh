#!/usr/bin/env bash
set -euo pipefail

if command -v docker-compose >/dev/null 2>&1; then
  DC=(docker-compose)
elif command -v docker >/dev/null 2>&1 && docker compose version >/dev/null 2>&1; then
  DC=(docker compose)
else
  echo "Docker Compose nao encontrado. Instale para subir o RabbitMQ localmente." >&2
  exit 1
fi

if ! command -v javac >/dev/null 2>&1; then
  echo "JDK nao encontrado (javac ausente). Instale um JDK 21+ ou use 'docker compose up --build'." >&2
  exit 1
fi

echo "Subindo o RabbitMQ..."
"${DC[@]}" up -d rabbitmq

echo "Aguardando o RabbitMQ ficar pronto..."
if ! "${DC[@]}" up -d --wait rabbitmq; then
  echo "RabbitMQ nao ficou pronto. Ultimos logs:" >&2
  "${DC[@]}" logs rabbitmq --tail=100 >&2
  exit 1
fi

echo "RabbitMQ pronto. Iniciando a aplicacao..."
exec ./mvnw spring-boot:run
