#!/bin/bash

set -e

echo "Starting Maven Tests..."

mvn clean test

echo "Generating Allure Report..."

ls -R target

echo "Tests completed successfully"

RUN_ID=$(date +%Y%m%d-%H%M%S)

echo "Uploading Allure Report to S3..."

echo "RUN_ID: $RUN_ID"

aws s3 cp target/cucumber-report.html s3://ace-automation-bucket-2026-ap-southeast-2/reports/${RUN_ID}/cucumber-report.html

echo "Allure Report uploaded successfully to S3"

echo "s3://ace-automation-bucket-2026-ap-southeast-2/reports/${RUN_ID}/"
