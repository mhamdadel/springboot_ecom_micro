provider "aws" {
  region = var.aws_region
}

resource "aws_lambda_function" "service_discovery_lambda" {
  function_name = var.lambda_function_name
  handler       = "service_discovery_lambda.handler"
  runtime       = "nodejs14.x"
  timeout       = 10
  memory_size   = 256

  environment {
    variables = {
      SERVICE_ENDPOINT = var.service_endpoint
    }
  }

  filename         = var.lambda_source_code_path
  source_code_hash = filebase64sha256(var.lambda_source_code_path)

  role = aws_iam_role.lambda_role.arn
}

resource "aws_iam_role" "lambda_role" {
  name = "lambda_service_discovery_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect    = "Allow",
        Principal = {
          Service = "lambda.amazonaws.com"
        },
        Action    = "sts:AssumeRole"
      }
    ]
  })
}