# Delivery System

A distributed microservices application built with Spring Boot that handles order processing and delivery management with comprehensive observability through OpenTelemetry.

## Application Overview

This system consists of two core microservices that work together to process orders and manage deliveries:

### **Orders Service** (Port 6000)
- Handles order creation and retrieval.
- Stores order data in Redis.
- Communicates with the Delivery Service to initiate deliveries.
- Manages order status transitions (CREATED → IN_DELIVERY)

### **Delivery Service** (Port 6001)
- Creates delivery records for orders.
- Generates unique delivery IDs.
- Processes delivery requests from the Orders Service.

### **Application Flow**
1. Client creates an order via POST `/orders`
2. Orders Service saves the order to Redis with status "CREATED"
3. Orders Service calls Delivery Service to create a delivery.
4. Delivery Service generates a unique delivery ID and returns delivery details.
5. Orders Service updates order status to "IN_DELIVERY"
6. Clients can retrieve orders via GET `/orders/{orderId}`

## Architecture Components

### **Core Services**
- **Orders Service**: Order management and orchestration.
- **Delivery Service**: Delivery processing and ID generation.
- **Redis**: Data storage for orders.

### **Observability Stack**
- **OpenTelemetry Collector**: Telemetry data aggregation.
- **Prometheus/Grafana Mimir**: Metrics storage and querying.
- **Grafana Tempo**: Distributed tracing.
- **Grafana Loki**: Log aggregation.
- **Grafana**: Visualization and dashboards.

## OpenTelemetry Infrastructure

### **Instrumentation Strategy**

**Orders Service:**
- Uses OpenTelemetry Java Agent for automatic instrumentation.
- Configured via `otel-agent.properties`
- Automatic trace generation for HTTP requests, database operations, and service calls.

**Delivery Service:**
- Uses Spring Boot OpenTelemetry starter for instrumentation.
- Manual metrics with Micrometer annotations (`@Timed`, `@Counted`)
- AspectJ-based metrics collection.

### **Telemetry Pipeline**
```
Services → OpenTelemetry Collector → Storage Backends
                                  ├── Prometheus/Mimir (Metrics)
                                  ├── Tempo (Traces)
                                  └── Loki (Logs)
```

### **Data Export Configuration**
- **Endpoint**: `http://otel-collector:4318`
- **Protocol**: OTLP over HTTP.
- **Data Types**: Metrics, Traces, and Logs.
- **Organization ID**: "paymentology" (for Prometheus/Mimir)

### **Storage Configuration**
- **Prometheus/Mimir**: 3-node cluster for high availability metrics storage.
- **Tempo**: Single instance with local storage.
- **Loki**: Storage for logs.

## Service Level Indicators (SLIs) & Service Level Objectives (SLOs)

### **Key Metrics Tracked/SLIs**

#### **Orders Service Metrics:**
- `counter.orders.created.count.success` - Successful order creations.
- `counter.orders.retrieved.count.success` - Successful order retrievals.
- `counter.orders.received.count.failed` - Failed order retrievals.
- `counter.orders.delivery.created.count.success` - Successful delivery creations.
- `counter.orders.delivery.created.count.failed` - Failed delivery creations.
- `latencyInSeconds.orders.created` - Order creation latency.
- `latencyInSeconds.orders.received` - Order retrieval latency.
- `latencyInSeconds.orders.delivery.created` - Delivery creation latency.

#### **Delivery Service Metrics:**
- `counter.deliveries` - Total delivery requests.
- `latencyInSec.deliveries` - Delivery processing latency.

### **SLOs**

#### **Availability SLOs**
- **Orders Service**: 99.9% uptime
  - *As the orchestration layer, high availability is critical for business operations. This allows for ~43 minutes of downtime per month.*
- **Delivery Service**: 99.5% uptime
  - *Slightly lower than Orders Service since it's a downstream dependency. Orders can be queued if delivery service is temporarily unavailable.*
- **End-to-end Order Processing**: 99.5% success rate
  - *Accounts for both service dependencies and external factors. Balances user experience with realistic operational constraints.*

#### **Performance SLOs**
- **Order Creation**: < 500ms (95th percentile)
  - *Includes Redis write + delivery service call. Users expect quick order confirmation, but some latency acceptable for complex operations.*
- **Order Retrieval**: < 200ms (95th percentile)
  - *Simple Redis lookup should be very fast. Users expect immediate access to their order information.*
- **Delivery Creation**: < 1000ms (95th percentile)
  - *Involves ID generation and logging. Slightly higher tolerance since it's an internal operation not directly visible to end users.*
- **End-to-end Order Processing**: < 2000ms (95th percentile)
  - *Complete order flow including all service calls and status updates. Provides reasonable user experience while accounting for distributed system complexity.*

#### **Error Rate SLOs**
- **Order Creation Errors**: < 1%
  - *Core business function with high reliability expectations. Accounts for occasional Redis or delivery service failures.*
- **Order Retrieval Errors**: < 0.5%
  - *Simple read operations should have very low failure rates. Only fails if Redis is completely unavailable or data corruption occurs.*
- **Delivery Creation Errors**: < 2%
  - *Higher tolerance since orders can be retried and delivery creation is not immediately customer-facing. Accounts for network issues between services.*

## Getting Started

### **Prerequisites**
- Docker and Docker Compose
- Java 21
- Maven 3.9.9
- IntelliJ IDEA or any other Java IDE

### **Running the Application**

1. **Package the Services with maven using the commandline or with IntelliJ IDEA**
   ```bash
   # Package Orders Service
   cd orders-service
   ./mvnw package
   
   # Package Delivery Service
   cd ../delivery-service
   ./mvnw package
   ```

1. **Build the Services:**
   ```bash
   # Build Orders Service
   cd orders-service
   docker build -t orders-service:1.0 .
   
   # Build Delivery Service
   cd ../delivery-service
   docker build -t delivery-service:1.0 .
   ```

2. **Start the Application & Observability Infrastructure:**
   ```bash
   cd docker
   docker-compose up -d
   ```

3. **Access Points:**
   - Orders Service: http://localhost:6000
   - Delivery Service: http://localhost:6001
   - Grafana Dashboard: http://localhost:3000
   - Prometheus/Mimir Metrics: http://localhost:8080
   - Tempo Traces: http://localhost:3200
   - Loki Logs: http://localhost:3100

### **API Usage**

To test the endpoints, use Postman, Insomnia or any other API Client.

#### **Create an Order:**
```bash
curl -X POST http://localhost:6000/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "ketra-456",
    "totalPrice": 150,
    "source": "Bunga",
    "destination": "Kigo"
  }'
```

#### **Retrieve an Order:**
```bash
curl http://localhost:6000/orders/order-679
```

## Technology Stack

### **Backend Technologies**
- **Spring Boot 3.4.2**: Application framework
- **Java 21**: Runtime environment
- **Redis**: Data storage

### **Observability Technologies**
- **OpenTelemetry**: Instrumentation and telemetry collection.
- **Grafana Stack**: Mimir, Tempo, Loki.
- **Micrometer**: Metrics collection framework.
- **AspectJ**: Aspect-oriented programming for metrics.

### **Infrastructure**
- **Docker**: Containerization.
- **Docker Compose**: Multi-container orchestration.
- **Maven**: Build automation.

## Monitoring and Alerting

### **Available Dashboards**
Access Grafana at http://localhost:3000 (anonymous access enabled) to view:
- Service metrics and performance dashboards.
- Distributed tracing visualization.
- Log correlation and analysis.

### **Health Checks**
Both services expose actuator endpoints:
- Orders Service: http://localhost:6000/actuator/metrics
- Delivery Service: http://localhost:6001/actuator/metrics

### **Telemetry Data**
Raw telemetry data is also exported to `/etc/telemetry-data.json` for debugging and analysis.

## Troubleshooting

### **Common Issues**
- **Service Communication**: Ensure all containers are on the same Docker network.
- **Redis Connection**: Verify Redis container is running and accessible.
- **OpenTelemetry**: Check collector logs if telemetry data isn't appearing.
- **Port Conflicts**: Ensure ports 3000, 3100, 3200, 6000, 6001, 6379, 8080 are available.

### **Logs and Debugging**
- Service logs are available via `docker logs <container-name>`
- OpenTelemetry Collector runs in debug mode for detailed telemetry logging.
- All telemetry data flows through the collector for centralized debugging.