#!/usr/bin/env bash
set -euo pipefail

# Detect Docker Compose command (docker-compose or `docker compose`)
if command -v docker-compose >/dev/null 2>&1; then
  DC=(docker-compose)
elif command -v docker >/dev/null 2>&1 && docker compose version >/dev/null 2>&1; then
  DC=(docker compose)
else
  echo "Docker Compose not found. Install docker-compose or ensure 'docker compose' is available." >&2
  exit 1
fi

echo "Starting RabbitMQ container..."
"${DC[@]}" up -d rabbitmq

# Try to get container id
cid="$("${DC[@]}" ps -q rabbitmq || true)"
if [ -z "$cid" ]; then
  cid=$(docker ps -q --filter "name=meu_rabbitmq" || true)
fi

echo "Waiting for RabbitMQ to become healthy (will wait up to 60s)..."
retries=60
count=0
while [ $count -lt $retries ]; do
  if [ -n "$cid" ]; then
    status=$(docker inspect --format='{{.State.Health.Status}}' "$cid" 2>/dev/null || echo unknown)
  else
    status="unknown"
  fi
  if [ "$status" = "healthy" ]; then
    echo "RabbitMQ is healthy."
    break
  fi
  sleep 1
  count=$((count+1))
  cid="$("${DC[@]}" ps -q rabbitmq || true)"
  echo -n "."
done

if [ $count -ge $retries ]; then
  echo "\nRabbitMQ did not become healthy after ${retries}s. Showing last logs:" >&2
  "${DC[@]}" logs rabbitmq --tail=200 || true
  exit 1
fi

# Start the Spring Boot application using the project's wrapper
echo "Starting application..."
exec ./mvnw spring-boot:run
