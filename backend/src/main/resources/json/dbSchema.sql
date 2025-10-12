-- Orders table
CREATE TABLE orders (
id INT PRIMARY KEY,
product VARCHAR(255) NOT NULL,
amount DOUBLE,
order_date DATE
);

-- Customers table
CREATE TABLE customers (
id INT PRIMARY KEY,
name VARCHAR(255),
email VARCHAR(255),
created_at TIMESTAMP NOT NULL
);