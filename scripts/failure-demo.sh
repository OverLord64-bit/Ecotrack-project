#!/usr/bin/env bash
# Recovery demonstration for the services in this repository: stop each one, start it again, and check that it
# comes back healthy and that the municipal system still holds ONE record (nothing lost, nothing duplicated).
# Needs the stack from `docker compose up` and the docker CLI.  Usage: bash scripts/failure-demo.sh
set -euo pipefail
cd "$(dirname "$0")"
source ./lib.sh
COMPOSE="docker compose -f ../docker-compose.yml"

wait_for_url "$MUNICIPAL/actuator/health" "municipal-service"
wait_for_url "$COLLECTION_ACTUATOR/actuator/health" "collection-service"

echo "== A. municipal-service goes down and comes back"
RECORD="$(uuid)"; REPORT="$(uuid)"
REF="$(soap_submit "$RECORD" "$REPORT" | soap_reference)"
assert_contains "$REF" "MUN-" "record stored before the outage: $REF"
$COMPOSE stop municipal-service >/dev/null
pass "municipal-service stopped"
curl -fsS --max-time 3 "$MUNICIPAL/actuator/health" >/dev/null 2>&1 && fail "municipal-service still answers" || pass "SOAP endpoint is unreachable while stopped"
$COMPOSE start municipal-service >/dev/null
wait_for_url "$MUNICIPAL/actuator/health" "municipal-service"
assert_eq "$(soap_record_found "$RECORD")" "found>true<" "the record survived the restart (PostgreSQL)"
DUP="$(soap_submit "$RECORD" "$REPORT")"
assert_contains "$DUP" "duplicate>true<" "a replay is recognised as a duplicate: still ONE record"
assert_contains "$DUP" "$REF" "and returns the same reference"

echo "== B. collection-service goes down and comes back"
$COMPOSE stop collection-service >/dev/null
pass "collection-service stopped"
curl -fsS --max-time 3 "$COLLECTION_ACTUATOR/actuator/health" >/dev/null 2>&1 && fail "collection-service still answers" || pass "collection-service is unreachable while stopped"
$COMPOSE start collection-service >/dev/null
wait_for_url "$COLLECTION_ACTUATOR/actuator/health" "collection-service"
assert_contains "$(curl -fsS "$COLLECTION_ACTUATOR/actuator/health")" '"status":"UP"' "collection-service is UP again"

echo; echo "Failure recovery demonstrated."
