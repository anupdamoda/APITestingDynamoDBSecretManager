#!/bin/bash

set -e

echo "Starting Maven Tests..."

mvn clean test

echo "Generating Allure Report..."

ls -R target

echo "Tests completed successfully"

TEST_EXIT_CODE=$?

RUN_ID=$(date +%Y%m%d-%H%M%S)

aws s3 cp target/cucumber-html-reports s3://ace-automation-bucket-2026-ap-southeast-2/reports/RUN_ID/ --recursive

exit $TEST_EXIT_CODE