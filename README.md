# BankBack

## Descripción del proyecto
“BankBack” es una API REST para la gestión de cuentas bancarias (corriente, ahorro y tarjeta de crédito) con control de usuarios (Admin, AccountHolder y Third-Party) y transferencias seguras. Nació como proyecto final del Bootcamp de Java en Ironhack para demostrar dominio de Spring Boot, JPA y buenas prácticas de arquitectura.

---

## Diagrama de Clases
![Class Diagram](/assets/UML_Banking-system.png)

---

## Setup
```bash
# 1. Clona el repo
git clone https://github.com/[📝 TU-USUARIO]/BankBack.git
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

| Método | Endpoint | Rol | Descripción |
|--------|----------|-----|-------------|
| GET | `/holder/accounts` | Holder | Lista todas mis cuentas |
| GET | `/holder/account/{id}` | Holder | Detalle de cuenta |
| POST | `/holder/transfer` | Holder | Transferencia a otra cuenta |
| PATCH | `/holder/{id}/status` | Holder | Cambia estado de la cuenta |
| DELETE | `/holder/{id}` | Holder | Cierra la cuenta |
| POST | `/admin/checking` | Admin | Crea cuenta corriente |
| POST | `/admin/savings` | Admin | Crea cuenta de ahorro |
| POST | `/admin/credit-card` | Admin | Crea tarjeta de crédito |
| GET | `/admin/accounts` | Admin | Lista todas las cuentas |
| ... | ... | ... | *(ver Swagger para el resto)* |

---

## Enlaces extra
- **Kanban/Trello**: [📝 URL]
- **Wireframes / Figma**: [📝 URL]
- **Presentación de diapositivas**: [📝 URL] <!-- cuando la subas -->
- **Vídeo demo**: [📝 URL]

---

## Future Work
- 💳 Apple/Google Pay integration
- 🌍 Localización multilingüe (i18n)
- 📈 Reporte financiero descargable en PDF

---

## Resources
- “Spring Boot, 3ª Ed.” — Craig Walls
- Baeldung & Stack Overflow (salvavidas diario)
- Documentación oficial MySQL & JWT

---

## Team members
| Nombre             | Rol     | GitHub |
|--------------------|---------|--------|
| [📝 Paola Montaño] | Student | [📝 @tuusuario] |