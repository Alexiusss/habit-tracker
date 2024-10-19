# HW_2

---

### Stack:
- **JDK**: 17
- **Apache Maven**
- **Lombok**
- **JUnit**
- **PostgreSQL**
- **Liquibase**
- **Testcontainers**
- **Mockito**
- **AssertJ**
- **HikariCP**
---

### Setup Instructions

1. Clone the repository:
    ```bash
    git clone https://github.com/Alexiusss/habit-tracker/ -b HW_2
    cd habit-tracker
    ```

2. Create a `.env` file in the project root with the following credentials:
   - `POSTGRES_USER`
   - `POSTGRES_PASSWORD`
   - `POSTGRES_DB`
   - `PGDATA_PATH`

3. Start the Docker container:
    ```bash
    docker compose up -d
    ```

4. Build the project using Maven:
    ```bash
    mvn clean package
    ```

5. Copy your `.env` file into the `/target/` folder:
    ```bash
    cp .env ./target/
    ```

6. Run the application:
    ```bash
    java -jar ./target/*.jar
    ```

    Credentials for admin access:
    ```
   login: admin@gmail.com
   password: admin777
   ```
   Credentials for user access:
    ```
   login: user@gmail.com
   password: user555
   ```