# API Testing with DynamoDB & Secrets Manager

An automated BDD API testing framework for an online expense manager backend. This project demonstrates modern testing practices using Cucumber, REST Assured, and AWS services integration—including S3 report generation and CI/CD pipeline automation.

## 🎯 Overview

This repository contains comprehensive API tests for an expense management application, showcasing:

- **Behavior-Driven Development (BDD)** using Cucumber with Gherkin syntax
- **REST API testing** with REST Assured and Hamcrest matchers
- **AWS integration** – credentials from Secrets Manager, test data from DynamoDB, reports to S3
- **Containerized execution** via Docker for consistent test runs
- **CI/CD pipeline** integration with AWS CodeBuild, ECR, and ECS
- **S3 Report Generation** – automated test report uploads to AWS S3

## 📋 Features

- ✅ User authentication (register, login, JWT token validation)
- ✅ Expense management (create, list, validation scenarios)
- ✅ Secure credential retrieval from AWS Secrets Manager (no hardcoded secrets)
- ✅ Dynamic test data from DynamoDB
- ✅ Configurable via `config.properties`, system properties, or environment variables
- ✅ Docker containerization for reproducible test environments
- ✅ Automated pipeline with AWS CodeBuild → ECR → ECS
- ✅ **NEW:** Automated test report uploads to AWS S3
- ✅ **NEW:** AWS CLI integration for S3 operations

## 🛠️ Prerequisites

### Local Development

- **Java 11+** (the project targets Java 11)
- **Maven 3.9+**
- **AWS credentials** configured locally (via `~/.aws/credentials` or IAM role)
- **AWS account** with access to:
  - Secrets Manager (for test credentials)
  - DynamoDB (for test data)
  - S3 (for test reports)

### Docker / Pipeline

- **Docker** (for local container testing)
- **AWS CLI** (for pipeline execution and S3 operations)
- **ECR repository** for image storage
- **ECS cluster** for task execution
- **S3 bucket** for test reports

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/anupdamoda/APITestingDynamoDBSecretManager.git
cd APITestingDynamoDBSecretManager
```

### 2. Configure AWS Secrets

Ensure you have the following AWS Secrets Manager secret:

**Secret Name:** `onlineexpensemanager/credentials`

**Secret Value (JSON):**
```json
{
  "aceonlineexpensemanager-username": "your-test-email@example.com",
  "aceonlineexpensemanager-password": "your-test-password"
}
```

### 3. Configure DynamoDB

Create a DynamoDB table named `OnlineExpenseManagerExpenses` in region `ap-southeast-2` with the following structure:

| Attribute | Type |
|-----------|------|
| ExpenseName | String |
| ExpenseCategory | String |
| ExpenseAmount | Number |

Add sample expense records for testing.

### 4. Update Configuration

Edit `src/test/resources/config.properties`:

```properties
base.url=https://your-api-endpoint.execute-api.us-east-1.amazonaws.com
aws.region=ap-southeast-2
aws.roleArn=arn:aws:iam::YOUR_ACCOUNT_ID:role/YourTestingRole
s3.bucket=your-test-reports-bucket
s3.region=ap-southeast-2
```

### 5. Run Tests Locally

```bash
mvn clean test
```

Tests will execute, retrieving credentials from Secrets Manager and test data from DynamoDB.

## 🐳 Docker Usage

### Build the Docker Image

```bash
docker build -t api-testing:latest .
```

### Run Tests in Docker

```bash
docker run --rm \
  -e AWS_REGION=ap-southeast-2 \
  -e AWS_ACCESS_KEY_ID=your_key \
  -e AWS_SECRET_ACCESS_KEY=your_secret \
  -e S3_BUCKET=your-test-reports-bucket \
  api-testing:latest
```

Or use AWS credentials from your host:

```bash
docker run --rm \
  -v ~/.aws/credentials:/root/.aws/credentials \
  -e S3_BUCKET=your-test-reports-bucket \
  api-testing:latest
```

**Note:** The Docker image now includes AWS CLI for S3 report operations.

## 📁 Project Structure

```
src/test/java/org/example/
├── api/                      # REST API wrappers
│   ├── AuthApi.java         # Authentication endpoints
│   └── ExpenseApi.java      # Expense management endpoints
├── steps/                     # Cucumber step definitions
│   ├── AuthSteps.java       # Auth scenario steps
│   └── ExpenseSteps.java    # Expense scenario steps
├── config/                    # Configuration management
│   └── Config.java          # Multi-source config loader
├── context/                   # Shared test state
│   └── ScenarioContext.java # Context between steps
└── utility/                   # AWS integrations
    ├── SecretManagerUtil.java # Secrets Manager client
    ├── DynamoDBUtil.java     # DynamoDB query utilities
    └── S3ReportUtil.java     # S3 report upload client

src/test/resources/
├── features/                  # Gherkin feature files
│   ├── auth_login.feature   # Login test scenarios
│   └── expense.feature      # Expense management scenarios
├── config.properties         # Base configuration
└── cucumber.properties       # Cucumber settings

Dockerfile                     # Container definition (updated with AWS CLI)
buildspec.yml                 # AWS CodeBuild pipeline
entrypoint.sh                 # Docker entry point
pom.xml                       # Maven dependencies
```

## 🧪 Test Scenarios

### Authentication Tests (`auth_login.feature`)

- ✅ Login with valid credentials returns a JWT token
- ✅ Token format validation (JWT structure with 3 parts)

### Expense Tests (`expense.feature`)

- ✅ Create expense with valid data from DynamoDB
- ✅ Expense validation (category required, amount must be positive)
- ✅ List expenses for authenticated user

**Run specific scenarios:**

```bash
# Run only login tests
mvn clean test -Dcucumber.filter.tags="@LoginTest"

# Run only database tests
mvn clean test -Dcucumber.filter.tags="@DatabaseTest"
```

## 📊 S3 Report Generation

Test reports are automatically generated and uploaded to S3 upon test completion.

**Report Location:** `s3://your-bucket/test-reports/[timestamp]/cucumber-report.html`

**S3 Configuration:**
```properties
s3.bucket=your-test-reports-bucket
s3.region=ap-southeast-2
```

Reports are organized by timestamp for easy tracking and retrieval. Access reports directly from the AWS S3 console or via AWS CLI:

```bash
# List all reports
aws s3 ls s3://your-test-reports-bucket/test-reports/ --recursive

# Download a specific report
aws s3 cp s3://your-test-reports-bucket/test-reports/[timestamp]/cucumber-report.html . --region ap-southeast-2
```

## 🔐 Configuration Hierarchy

Configuration is resolved in this order (first match wins):

1. **Environment variables** (e.g., `TEST_USER_EMAIL`, `S3_BUCKET`)
2. **System properties** (e.g., `-Dtest.user.password=value`)
3. **config.properties** file
4. **AWS Secrets Manager** (for credentials)

**Example: Override base URL and S3 bucket**

```bash
# Via environment variable
export BASE_URL=https://custom-api.example.com
export S3_BUCKET=custom-bucket
mvn test

# Via system property
mvn test -Dbase.url=https://custom-api.example.com -Ds3.bucket=custom-bucket
```

## 🔑 Key Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Cucumber | 7.15.0 | BDD test framework |
| REST Assured | 5.4.0 | HTTP client for API testing |
| JUnit 5 | 5.10.2 | Test execution engine |
| AWS SDK v2 | 2.25.40 | AWS service integrations (Secrets Manager, DynamoDB, S3) |
| Jackson | 2.16.1 | JSON parsing |
| Hamcrest | 2.2 | Assertion matchers |

See `pom.xml` for the complete dependency list.

## 🔄 AWS CodeBuild Pipeline

The `buildspec.yml` automates the entire test pipeline:

1. **Pre-build:** Login to Amazon ECR
2. **Build:** Construct Docker image with AWS CLI
3. **Push:** Upload to ECR repository
4. **Run:** Trigger ECS task to execute tests
5. **Report:** Automatically upload test reports to S3
6. **Poll:** Wait for task completion and check exit code

**Required CodeBuild environment variables:**

```
AWS_REGION=ap-southeast-2
ECR_REPOSITORY=api-testing
ECS_CLUSTER=your-cluster-name
ECS_TASK_DEFINITION=your-task-definition
ECS_SUBNET=subnet-xxxxx
ECS_SECURITY_GROUP=sg-xxxxx
S3_BUCKET=your-test-reports-bucket
```

## 🐛 Troubleshooting

### Issue: "Failed to load config.properties"

**Solution:** Ensure `config.properties` is in `src/test/resources/`

### Issue: "Missing config key" error

**Solution:** Check `config.properties` has all required keys or set via environment/system properties.

### Issue: Secrets Manager authentication fails

**Solution:** 
- Verify AWS credentials are configured: `aws sts get-caller-identity`
- Check IAM permissions for Secrets Manager
- Ensure secret name matches: `onlineexpensemanager/credentials`

### Issue: DynamoDB scan returns empty

**Solution:**
- Verify table exists: `aws dynamodb describe-table --table-name OnlineExpenseManagerExpenses`
- Confirm items exist: `aws dynamodb scan --table-name OnlineExpenseManagerExpenses`
- Check region matches in code: `Region.AP_SOUTHEAST_2`

### Issue: Docker build fails

**Solution:**
- Ensure Docker daemon is running
- Check Docker has sufficient disk space
- Verify Java/Maven versions in the base image: `maven:3.9.6-eclipse-temurin-17`
- Verify AWS CLI is properly included in the Docker image

### Issue: S3 report upload fails

**Solution:**
- Verify S3 bucket exists: `aws s3 ls s3://your-bucket --region ap-southeast-2`
- Check IAM permissions for S3 PutObject on the bucket
- Verify S3 bucket name is correct in configuration
- Ensure AWS credentials have proper S3 access

### Issue: Docker image lacks AWS CLI

**Solution:**
- Rebuild the Docker image: `docker build -t api-testing:latest .`
- Verify Dockerfile includes AWS CLI installation
- Check Docker build logs for errors

## 📊 Test Execution Output

After running tests, view results:

```bash
# HTML report (if configured)
mvn test

# View target directory
ls -R target/

# View S3 reports
aws s3 ls s3://your-bucket/test-reports/ --recursive --region ap-southeast-2
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-test`
3. Add tests in `src/test/resources/features/`
4. Implement steps in `src/test/java/org/example/steps/`
5. Commit and push: `git commit -m "Add: new test scenario"`
6. Open a Pull Request

## 📝 Best Practices

- ✅ Keep feature files readable and scenario-focused
- ✅ Use data-driven tests for multiple inputs
- ✅ Never hardcode credentials; use Secrets Manager
- ✅ Tag scenarios appropriately (`@LoginTest`, `@DatabaseTest`)
- ✅ Use descriptive assertion messages
- ✅ Clean up test data after execution when possible
- ✅ Store test reports in S3 for archival and compliance

## 🔗 Resources

- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [REST Assured Guide](https://rest-assured.io/)
- [AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/)
- [AWS S3 Documentation](https://docs.aws.amazon.com/s3/)
- [Hamcrest Matchers](https://hamcrest.org/)

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

Created by [anupdamoda](https://github.com/anupdamoda)

---

**Last Updated:** July 2026  
**Status:** Active Development  
**Recent Updates:** S3 report generation, AWS CLI integration, enhanced pipeline automation
