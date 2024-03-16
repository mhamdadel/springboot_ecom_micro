output "ec2_instance_ids" {
  value = [aws_instance.api_gateway_instance.id]  # Update with other EC2 instance IDs as needed
}
