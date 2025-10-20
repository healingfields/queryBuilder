-- Orders table
CREATE TABLE orders (
id INT PRIMARY KEY,
product VARCHAR(255) NOT NULL,
amount DOUBLE,
order_date DATE
);

INSERT INTO `orders` (`id`, `product`, `amount`, `order_date`) VALUES
(1, 'Alice', null, null);

-- Customers table
CREATE TABLE customers (
id INT PRIMARY KEY,
name VARCHAR(255),
email VARCHAR(255),
created_at TIMESTAMP NOT NULL
);