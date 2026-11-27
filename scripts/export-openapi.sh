#!/usr/bin/env bash
set -euo pipefail

# Report Service URL
BASE_URL="${REPORT_SERVICE_URL:-http://localhost:8081}"

# Locate project root
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd -- "$SCRIPT_DIR/.." && pwd)"

# Prepare output directory and file
OUTPUT_DIR="$ROOT_DIR/contracts/openapi"
OUTPUT_FILE="$OUTPUT_DIR/report-service.json"

mkdir -p "$OUTPUT_DIR"

echo "Exporting OpenAPI specification from ${BASE_URL}/v3/api-docs ..."

# Download OpenAPI specification
curl --fail --silent --show-error \
    "${BASE_URL}/v3/api-docs" \
    -o "$OUTPUT_FILE"

echo "OpenAPI specification exported successfully:"
echo "$OUTPUT_FILE"