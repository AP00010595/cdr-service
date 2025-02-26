# CDR Service
## Setup Instructions
1. Clone the repository.
2. Run `mvn clean install`.
3. Run `docker-compose up`.
4. Deploy to Kubernetes: kubectl apply -f k8s/
5. API available at `http://localhost:8080/api/charge-detail-records`.
6. Swagger UI available at `http://localhost:8080/swagger-ui.html`.

## Endpoints
- `POST /api/charge-detail-records` - Create a Charge Detail Record.
- `GET /api/charge-detail-records/{id}` - Get CDR by ID.
- `GET /api/charge-detail-records/vehicle/{vehicleId}` - Get CDRs by vehicle.
