module "ec2" {
  source = "./modules/ec2"

  aws_region          = var.aws_region
  ec2_instance_type   = var.ec2_instance_type
}