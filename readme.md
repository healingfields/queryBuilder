# Mini Query Builder

A simple **Query Builder** web application built with **Spring Boot** (backend) and **H2 in-memory database**.
It allows users to dynamically select tables, columns, and filters to execute queries and return results as JSON.

This project is a mini version designed for **learning and prototyping**, with a frontend (can be extended in Svelte) and a REST API backend.

---

## **Technologies Used**

* **Backend:** Java, Spring Boot, Spring Web, JPA, H2 Database, Lombok
* **Frontend:** Optional (Svelte / TailwindCSS)
* **Build Tools:** Maven
* **Database:** H2 (in-memory)

---

## **Project Structure**

```
src/main/java/com/example/querybuilder/
 ├─ controller/       # REST API endpoints
 ├─ dto/              # DTOs for query and filters
 ├─ model/            # JPA entities (User, Order)
 ├─ repository/       # JPA Repositories
 ├─ service/          # services
 ├─ config/           # DataSeeder for initial data
 └─ utils/            # Optional helpers
```

---

## **Sample Data**

The app initializes with **two tables**:

### User Table (`app_user`)

| id | name    | email                                             | age |
| -- | ------- | ------------------------------------------------- | --- |
| 1  | Alice   | [alice@example.com](mailto:alice@example.com)     | 28  |
| 2  | Bob     | [bob@example.com](mailto:bob@example.com)         | 35  |
| 3  | Charlie | [charlie@example.com](mailto:charlie@example.com) | 42  |

### Order Table (`orders`)

| id | product | amount | date       | user_id |
| -- | ------- | ------ | ---------- | ------- |
| 1  | Laptop  | 1200   | 2025-10-01 | 1       |
| 2  | Phone   | 800    | 2025-10-04 | 2       |
| 3  | Tablet  | 400    | 2025-10-05 | 3       |
| 4  | Monitor | 300    | 2025-10-03 | 1       |

---

## **Endpoints**

### 1️⃣ **List Tables**

```
GET /api/tables
```

**Response Example:**

```json
["users", "orders"]
```

---

### 2️⃣ **List Columns of a Table**

```
GET /api/columns?table={tableName}
```

**Example:**

```
GET /api/columns?table=app_user
```

**Response:**

```json
["id","name","email","age"]
```

---

### 3️⃣ **Execute Query**

```
POST /api/query
```

**Request Body Example:**

```json
{
  "table": "orders",
  "columns": ["product", "amount"],
  "filters": [
    {
      "column": "amount",
      "operator": "<",
      "value": "1000"
    }
  ]
}
```

**Response Example:**

```json
[
  ["Phone", 800],
  ["Tablet", 400],
  ["Monitor", 300]
]
```

**Notes:**

* `table` → entity/table name (`app_user` or `orders`)
* `columns` → list of column names to return
* `filters` → list of filter objects:

    * `column` → column name
    * `operator` → `=`, `<`, `>`, `like`
    * `value` → string value (numeric columns are parsed automatically)

---

#

4. Test endpoints using **Postman**, **curl**, or frontend app:

```bash
curl http://localhost:8080/api/tables
```

```bash
curl http://localhost:8080/api/columns?table=orders
```

```bash
curl -X POST http://localhost:8080/api/query \
-H "Content-Type: application/json" \
-d '{"table":"orders","columns":["product","amount"],"filters":[{"column":"amount","operator":"<","value":"1000"}]}'
```

---

## **Future Improvements**

* Add support for multiple filters with AND/OR logic.
* Add pagination and sorting for results.
* Support JOINs between tables.
* Add a frontend using **Svelte** for interactive query building.
* Validate table/column names to prevent SQL injection.
