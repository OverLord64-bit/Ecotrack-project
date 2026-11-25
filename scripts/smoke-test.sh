#!/usr/bin/env bash
# End-to-end test of the running stack (docker compose up --build) for the parts in this repository:
# SOAP (municipal-service: WSDL, Faults, idempotency), gRPC (collection-service, when grpcurl is installed)
# and the frontend image.  Usage: bash scripts/smoke-test.sh
set -euo pipefail
cd "$(dirname "$0")"
source ./lib.sh

echo "== 1. Everything is up"
wait_for_url "$MUNICIPAL/actuator/health" "municipal-service"
wait_for_url "$COLLECTION_ACTUATOR/actuator/health" "collection-service"
wait_for_url "$FRONTEND/" "frontend"

echo "== 2. SOAP contract (WSDL generated from the XSD, Faults)"
WSDL="$(curl -fsS "$MUNICIPAL/ws/municipal.wsdl")"
assert_contains "$WSDL" "SubmitCollectionRecord" "WSDL lists SubmitCollectionRecord"
assert_contains "$WSDL" "GetRecordStatus" "WSDL lists GetRecordStatus"
assert_contains "$(soap '<m:SubmitCollectionRecordRequest><m:recordId>not-a-uuid</m:recordId></m:SubmitCollectionRecordRequest>')" "Fault" "a message that breaks the XSD returns a SOAP Fault"
FUTURE="$(soap '<m:SubmitCollectionRecordRequest><m:recordId>11111111-1111-1111-1111-111111111111</m:recordId><m:reportId>11111111-1111-1111-1111-111111111111</m:reportId><m:collectionId>11111111-1111-1111-1111-111111111111</m:collectionId><m:completedAt>2099-01-01T10:00:00Z</m:completedAt></m:SubmitCollectionRecordRequest>')"
assert_contains "$FUTURE" "RecordRejectedFault" "a business rule violation returns RecordRejectedFault"

echo "== 3. The municipal system stores a record exactly once"
RECORD="$(uuid)"; REPORT="$(uuid)"
assert_eq "$(soap_record_found "$RECORD")" "found>false<" "an unknown record is not found"
FIRST="$(soap_submit "$RECORD" "$REPORT")"
assert_contains "$FIRST" "accepted>true<" "a valid record is accepted"
assert_contains "$FIRST" "duplicate>false<" "the first submission is not a duplicate"
REF="$(echo "$FIRST" | soap_reference)"
assert_contains "$REF" "MUN-" "municipal reference received: $REF"
assert_eq "$(soap_record_found "$RECORD")" "found>true<" "GetRecordStatus finds the record"
DUP="$(soap_submit "$RECORD" "$REPORT")"
assert_contains "$DUP" "duplicate>true<" "re-sending the same record is flagged duplicate"
assert_contains "$DUP" "$REF" "and returns the same reference"

echo "== 4. gRPC (collection-service, server reflection)"
if command -v grpcurl >/dev/null; then
  assert_contains "$(grpcurl -plaintext localhost:9090 list)" "ecotrack.collection.v1.CollectionService" "reflection lists CollectionService"
else
  echo "  skipped: grpcurl not installed (bash scripts/grpcurl-examples.sh exercises every method)"
fi

echo "== 5. Health"
for url in "$MUNICIPAL/actuator/health" "$COLLECTION_ACTUATOR/actuator/health"; do
  assert_contains "$(curl -fsS "$url")" '"status":"UP"' "$url is UP"
done

echo; echo "All smoke tests passed."
