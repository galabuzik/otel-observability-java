variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "eu-north-1"
}

variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
  default     = "paymentology-demo"
}

variable "orders_service_replicas" {
  description = "Number of replicas for orders service"
  type        = number
  
  validation {
    condition     = var.orders_service_replicas >= 1 && var.orders_service_replicas <= 4
    error_message = "Replica count must be between 1 and 4."
  }
}

variable "delivery_service_replicas" {
  description = "Number of replicas for delivery service"
  type        = number
  
  validation {
    condition     = var.delivery_service_replicas >= 1 && var.delivery_service_replicas <= 4
    error_message = "Replica count must be between 1 and 4."
  }
}