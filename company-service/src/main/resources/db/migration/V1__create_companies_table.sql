CREATE TABLE companies (
    id SERIAL PRIMARY KEY,
    company_name VARCHAR(50) NOT NULL,
    budget DECIMAL(10, 2),
    user_ids JSONB NOT NULL DEFAULT '[]'
);