terraform {
  required_providers {
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.12"
    }
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  required_version = ">= 1.0"
}

provider "aws" {
  region = var.aws_region
  
  default_tags {
    tags = {
      Environment = "production"
      Project     = "paymentology-microservices"
      ManagedBy   = "terraform"
    }
  }
}

data "aws_eks_cluster" "cluster" {
  name = var.cluster_name
}

provider "helm" {
  kubernetes {
    host                   = data.aws_eks_cluster.cluster.endpoint
    cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority[0].data)
    
    exec {
      api_version = "client.authentication.k8s.io/v1beta1"
      command     = "aws"
      args        = ["eks", "get-token", "--cluster-name", var.cluster_name, "--region", var.aws_region]
    }
  }
}

resource "helm_release" "microservices_prod" {
  name      = "deliverysystem-prod"
  chart     = "../../helm-chart"
  namespace = "default"

  wait    = true
  timeout = 300

  set {
    name  = "ordersService.image.tag"
    value = "latest"
  }

  set {
    name  = "ordersService.image.pullPolicy"
    value = "Always"
  }

  set {
    name  = "ordersService.replicaCount"
    value = var.orders_service_replicas
  }

  set {
    name  = "deliveryService.image.tag"
    value = "latest"
  }

  set {
    name  = "deliveryService.image.pullPolicy"
    value = "Always"
  }

  set {
    name  = "deliveryService.replicaCount"
    value = var.delivery_service_replicas
  }

  set {
    name  = "ordersService.env.ENVIRONMENT"
    value = "production"
  }

  set {
    name  = "deliveryService.env.ENVIRONMENT"
    value = "production"
  }
}