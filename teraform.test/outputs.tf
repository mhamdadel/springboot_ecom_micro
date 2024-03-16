module "ec2" {
  source = "./modules/ec2"

  aws_region        = var.aws_region
  ec2_instance_type = var.ec2_instance_type
}

output "api_gateway_ec2_instance_id" {
  value = module.ec2.ec2_instance_id
}
