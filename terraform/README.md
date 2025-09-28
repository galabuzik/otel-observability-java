#  Deploy Helm Chart using Terraform

This repository contains Terraform configurations for deploying microservices (Orders and Delivery services) to AWS EKS using a Helm chart across multiple environments.

## Repository Structure

```
├── environments/
│   ├── dev/                    # Development environment
│   ├── staging/                # Staging environment
│   └── prod/                   # Production environment
├── helm-chart/                 # Helm chart templates
│   ├── Chart.yaml
│   ├── values.yaml
│   └── templates/
│       ├── orders-service.yaml
│       └── delivery-service.yaml
└── setup-state-backend.sh      # Script to initialize Terraform state backend
```

## Quick Start

1. **Initialize Terraform state backend** (first-time setup)
   ```bash
   chmod +x setup-state-backend.sh
   ./setup-state-backend.sh
   ```

2. **Deploy to an environment** (e.g., dev)
   ```bash
   cd environments/dev
   terraform init
   terraform plan
   terraform apply
   ```

## Environment Configuration

### Development Environment
- **Replicas**: 1 per service

### Staging Environment
- **Replicas**: 1 per service
### Production Environment
- **Replicas**: 3 per service (high availability)
- **Validation**: Stricter replica count limits (1-4)

## Terraform Best Practices Implemented

### 1. **Remote State Management**
-  S3 backend with encryption enabled
-  DynamoDB table for state locking
-  Versioning enabled for state recovery

### 2. **Environment Separation**
-  Separate directories for each environment
-  Environment-specific `terraform.tfvars`
-  Isolated state files per environment

### 3. **Input Validation**
-  Variable validation rules for replica counts
-  Type constraints on all variables
-  Default values where appropriate

### 4. **Resource Tagging**
-  Consistent tagging strategy across all resources

### 5. **Provider Version Constraints**
- Pinned provider versions using `~>` for safe updates
- Minimum Terraform version specified
- Required providers explicitly declared

### 6. **Security**
- Encrypted Terraform state

### 7. **Modularity**
- DRY principle with shared Helm chart
- Environment-specific variable overrides

### 8. **Documentation**
- Meaningful output values

## Customization

### Scaling Services
Modify replica counts in the respective `terraform.tfvars` file:

```hcl
# environments/{env}/terraform.tfvars
orders_service_replicas = 2
delivery_service_replicas = 3
```

### Resource Limits

Add environment-specific resource limits blocks in `main.tf`

```hcl
# E.g: Higher resources for production
set {
  name  = "ordersService.resources.requests.memory"
  value = "512Mi"
}

set {
  name  = "ordersService.resources.limits.memory"
  value = "2Gi"
}
```
Alternatively, create environment-specific values files:

```yaml
# helm-chart/values-prod.yaml
ordersService:
  resources:
    requests:
      memory: "512Mi"
      cpu: "500m"
    limits:
      memory: "2Gi"
      cpu: "2000m"}
```

##  Available Commands

### Common Terraform Operations

```bash
# Initialize Terraform
terraform init

# Plan changes
terraform plan

# Apply changes
terraform apply

# Show current state
terraform show

# List resources
terraform state list

# Destroy infrastructure
terraform destroy
```

### Helm Operations

```bash
# Check Helm releases
helm list -A

# Get release status
helm status deliverysystem-{env}

```

## Outputs

Each environment provides the following outputs:
- `helm_release_name`: Name of the deployed Helm release
- `helm_release_status`: Current status of the deployment
- `environment`: Environment name
- `service_replicas`: Replica count per service

## Important Notes

1. **Image Tags**: Currently using `latest` tag with `Always` pull policy for this demo. In real applications, consider using specific tags.

2. **Namespace**: All services deploy to the `default` namespace for this demo. In real applications, consider using environment-specific namespaces.