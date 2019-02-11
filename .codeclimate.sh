#!/bin/bash

curl -L https://codeclimate.com/downloads/test-reporter/test-reporter-latest-linux-amd64 > ./cc-test-reporter
chmod +x ./cc-test-reporter
./cc-test-reporter before-build

find . -type f -iname "jacocoTestReport.xml" -print0 | while IFS= read -r -d $'\0' report; do
  PROJECT=$(dirname "${report}" | cut -d "/" -f2)
  TYPE=$(dirname "${report}" | rev | cut -d "/" -f1 | rev)

  JACOCO_SOURCE_PATH="$PROJECT/src/main/java"

  export JACOCO_SOURCE_PATH;
  echo "Generating coverage from $report with source_path $JACOCO_SOURCE_PATH"
  ./cc-test-reporter format-coverage $report --input-type jacoco --output coverage/codeclimate.$PROJECT.$TYPE.json
done

./cc-test-reporter sum-coverage coverage/codeclimate.*.json

./cc-test-reporter upload-coverage
