# BankBack

## Descripción del proyecto
“BankBack” es una API REST para la gestión de cuentas bancarias (corriente, ahorro y tarjeta de crédito) con control de usuarios (Admin, AccountHolder y Third-Party) y transferencias seguras. Nació como proyecto final del Bootcamp de Java en Ironhack para demostrar dominio de Spring Boot, JPA y buenas prácticas de arquitectura.

---

## Diagrama de Clases
![Class Diagram](/assets/BankBack_UML.png)

---

## Setup
```bash
# 1. Clona el repo
git clone https://github.com/979-Pao/Bank-Back.git
cd BankBack

# 2. Configura variables (application-dev.properties)
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bankback
SPRING_DATASOURCE_USERNAME=[📝]
SPRING_DATASOURCE_PASSWORD=[📝]
JWT_SECRET=[📝 clave-muy-secreta]

# 3. Arranca el servidor
./mvnw spring-boot:run
```

---

## Tecnologías utilizadas
- **Java 17**
- **Spring Boot 3.5** (Web, Data JPA, Security)
- **MySQL 8** + Flyway
- **JWT + Bearer Auth**
- **Maven**, **Lombok**, **JUnit 5 / Spring Test**
- **Swagger/OpenAPI** para documentación de endpoints

---

## Controladores y rutas

| Rol    | Endpoint                      | Método |Descripción                                          |
|--------|-------------------------------|--------|------------------------------------------------------|
| Auth   | `/auth/login`                 |    POST     |Iniciar sesión y recibir un JWT Válido               |
| Admin  | `/admin/checking`             |    POST    |Crea cuenta corriente o student                      |
| Admin  | `/admin/savings`              |   POST      |Crea cuenta de ahorro                                |
| Admin  | `/admin/credit-card`          |   POST      |Crea tarjeta de crédito                              |
| Admin  | `/admin/holders`              |   POST     |Crea un titular de cuenta (AccountHolder)            |
| Admin  | `/admin/third-party`          |    POST     |Crea una pasarela de pago (Thirdparty)               |
| Admin  | `/admin/accounts`             |    GET    |Lista todas las cuentas                              |
| Admin  | `/admin/holders`              |    GET    |Lista todos los titulares (AccountHolder)            |
| Admin  | `/admin/third-parties`        |   GET     |Lista todos los usuarios (Thirdparty)                |
| Admin  | `/admin/accounts/{id}/balance` |    PUT     |Actualizar el saldo de una cuenta especifica         |
| Admin  | `/admin/accounts/{id}`        |   DELETE     |Cierra la cuenta por su ID                           |
| Holder  | `/holder/transfer`            |    POST     |Transferencia a otra cuenta                          |
| Holder    | `/holder/accounts`            |   GET     |Lista todas mis cuentas                              |
| Holder    | `/holder/account/{id}`        |   GET    |Detalle de cuenta                                    |
| Holder  | `/holder/{id}/status`         |   PATCH     |Cambia estado de la cuenta                           |
| Holder | `/holder/{id}`                |   DELETE     |Cierra la cuenta                                     |
| ThirdParty   | `/thirdparty/refund`          |    POST     |Recibe dinero en una cuenta usando hashKey           |
| ThirdParty   | `/thirdparty/payment`         |    POST    |Envia dinero desde una cuenta usando hashKey         |
| ThirdParty    | `/thirdparty/transactions`    |     GET   |Consulta todas las transaciones asociadas al hashKey |
|ThirdParty | `/thirdparty/{id}`          |     DELETE   |Elimina un usuario ThirdParty                        |
---

## Enlaces extra
- **Kanban/Trello**: [📝 URL]
- **Presentación de diapositivas**: [📝 URL] <!-- cuando la subas -->
- **Vídeo demo**: [📝 URL]

---

## Future Work
- 💳 Apple/Google Pay integration
- 📈 Reporte financiero descargable en PDF

---

## Resources
- “Spring Boot, 3ª Ed.” — Craig Walls
- Baeldung & Stack Overflow (salvavidas diario)
- Documentación oficial MySQL & JWT

---

## Team members
| Nombre           | Rol     | GitHub      |
|------------------|---------|-------------|
| ☺️ Paola Montaño | Student | 📝 @979-Pao |