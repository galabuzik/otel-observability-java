terraform {
  backend "s3" {
    bucket         = "paymentology-terraform-state"
    key            = "microservices/staging/terraform.tfstate"
    region         = "eu-north-1"
    encrypt        = true
    dynamodb_table = "terraform-state-lock"
  }
}