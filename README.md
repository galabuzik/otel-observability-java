OpenTelemetry, Helm, Kubernetes & Terraform Demo

A comprehensive demonstration of modern microservices architecture with full observability, containerization, and infrastructure-as-code deployment strategies.

##  Overview

This repository showcases a complete end-to-end delivery system built with microservices architecture, featuring:

- **Two core business services** (Orders & Delivery)
- **Complete observability stack** with OpenTelemetry, Grafana, Prometheus/Mimir, Tempo, and Loki
- **Multiple deployment strategies** using Docker Compose, Kubernetes, Helm, and Terraform

## Architecture Components

### Core Services
- **Orders Service**: RESTful API for order management with Redis storage
- **Delivery Service**: Handles delivery operations and ID generation
- **Redis**: In-memory data store for order persistence

### Observability Stack
- **OpenTelemetry Collector**: Centralized telemetry collection and routing
- **Grafana**: Unified visualization and dashboards
- **Prometheus/Mimir**: Metrics storage and querying
- **Tempo**: Distributed tracing backend
- **Loki**: Log aggregation and analysis

### Infrastructure
- **AWS EKS**: Kubernetes cluster management
- **AWS ECR**: Container image registry
- **AWS ALB**: Application Load Balancer with path-based routing

## Deployment Options

### 1. Local Development (Docker Compose)
```bash
cd java/docker
docker-compose up -d
```
**Use Case**: Local development and testing
**Access**: http://localhost:3000 (Grafana), http://localhost:6000 (Orders), http://localhost:6001 (Delivery)

### 2. Kubernetes Direct Deployment
```bash
cd kubernetes
kubectl apply -f .
```
**Use Case**: Direct Kubernetes deployment without Helm

### 3. Helm Chart Deployment
```bash
cd kubernetes
helm install delivery-system ./application-helm-chart/
```
**Use Case**: Templated deployments with configuration management
**Features**: Environment-specific values, easy upgrades

### 4. Terraform + Helm
```bash
cd terraform/environments/prod
terraform init && terraform apply
```
**Use Case**: Infrastructure as Code with state management
**Features**: Multi-environment support, automated deployments, state locking

## Documentation

Each directory contains detailed README files:
- [`java/README.md`](java/README.md) - Development setup and Docker Compose
- [`kubernetes/README.md`](kubernetes/README.md) - Kubernetes deployment options
- [`terraform/README.md`](terraform/README.md) - Infrastructure as Code setup
