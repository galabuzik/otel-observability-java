output "helm_release_name" {
  description = "Name of the Helm release"
  value       = helm_release.microservices_prod.name
}

output "helm_release_status" {
  description = "Status of the Helm release"
  value       = helm_release.microservices_prod.status
}

output "environment" {
  description = "Environment name"
  value       = "production"
}

output "total_replicas" {
  description = "Total number of replicas across both services"
  value       = var.orders_service_replicas + var.delivery_service_replicas
}