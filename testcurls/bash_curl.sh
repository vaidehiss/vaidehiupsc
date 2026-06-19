#!/bin/bash
#

GQLFILE=$1

echo $GQLFILE

curl -s -X POST http://localhost:8080/ora/graphql \
  -H "Content-Type: application/json" \
  --data-binary @"$GQLFILE" | jq .
