module "lambda" {
  source = "./modules/lambda"

  aws_region            = var.aws_region
  lambda_function_name  = var.lambda_function_name
  lambda_source_code_path = var.lambda_source_code_path
  service_endpoint      = var.service_endpoint
}