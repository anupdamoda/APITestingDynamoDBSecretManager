#!/bin/bash

set -e

echo "Starting Maven Tests..."

mvn clean test

echo "Generating Allure Report..."

ls -R target

echo "Tests completed successfully"