provider "aws" {
  region = var.aws_region
}

resource "aws_instance" "api_gateway_instance" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = var.ec2_instance_type
  tags = {
    Name = "ApiGatewayInstance"
  }
}

# Repeat the above resource block for other EC2 instances as needed
