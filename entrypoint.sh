#!/bin/bash

set -e

echo "Starting Maven Tests..."

mvn clean test

echo "Generating Allure Report..."

mvn allure:report

echo "Listing results..."

ls -R target

echo "Tests completed successfully"