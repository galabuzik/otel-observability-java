output "helm_release_name" {
  description = "Name of the Helm release"
  value       = helm_release.microservices_staging.name
}

output "helm_release_status" {
  description = "Status of the Helm release"
  value       = helm_release.microservices_staging.status
}

output "helm_release_version" {
  description = "Version of the Helm release"
  value       = helm_release.microservices_staging.version
}

output "environment" {
  description = "Environment name"
  value       = "staging"
}

output "service_replicas" {
  description = "Number of replicas per service"
  value = {
    orders_service   = var.orders_service_replicas
    delivery_service = var.delivery_service_replicas
  }
}