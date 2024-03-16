variable "aws_region" {
  description = "AWS region where resources will be deployed"
  default     = "us-east-1"
}

variable "lambda_function_name" {
  description = "Name for the Lambda function"
  default     = "ServiceDiscoveryLambda"
}

variable "lambda_source_code_path" {
  description = "Path to the Lambda function code zip file"
  default     = "path/to/your/lambda/code.zip"
}

variable "service_endpoint" {
  description = "Service endpoint for Service Discovery Lambda"
  default     = "http://example.com/api"
}

variable "ec2_instance_type" {
  description = "EC2 instance type for other services"
  default     = "t2.micro"
}