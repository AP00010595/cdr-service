CREATE TABLE IF NOT EXISTS charge_detail_record (
                                                     id SERIAL PRIMARY KEY,
                                                     session_id VARCHAR(255) UNIQUE NOT NULL,
    vehicle_id VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL
    );
