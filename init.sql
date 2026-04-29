CREATE TABLE IF NOT EXISTS input (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending'
);

CREATE TABLE IF NOT EXISTS result (
    id SERIAL PRIMARY KEY,
    input_id INT REFERENCES input(id),
    worker_identifier VARCHAR(50) NOT NULL,
    result TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserción de datos
INSERT INTO input (description)
SELECT 'task ' || generate_series(1, 1000000);

CREATE INDEX idx_input_status_pending
ON input(status)
WHERE status = 'pending';
