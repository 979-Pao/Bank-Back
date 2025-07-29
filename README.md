# BankBack

## Descripción del proyecto
“BankBack” es una API REST para la gestión de cuentas bancarias (corriente, ahorro y tarjeta de crédito) con control de usuarios (Admin, AccountHolder y Third-Party) y transferencias seguras. 
Nació como proyecto final del Bootcamp de Java en Ironhack para demostrar dominio de Spring Boot, JPA y buenas prácticas de arquitectura.

---

## Características clave

|  Módulo              | Descripción                                                                |
|-----------------------|---------------------------------------------------------------------------------|
| Gestión de cuentas    | CRUD completo para Checking, Student Checking, Savings y Credit Card.           |
| Motor de transferencias| Transferencias internas validadas con reglas de negocio (saldo, límite diario). |
| Terceros seguros      | Pasarelas de pago (Third‑Party) autenticadas por X‑Hashed‑Key + secretKey.      |
| Seguridad JWT         | Login que devuelve token Bearer firmado; filtros por rol.                      |
| Control de fraude     | Servicio de FraudDetection pluggable (lógica simplificada + tests).             |
| Pruebas automáticas   | > 80 % lines covered: unit (Mockito) + integration (MockMvc & H2 in-memory).   |

---

## Diagrama de Clases
![Class Diagram](/assets/BankBack_UML.png)

---

## Arquitectura a grandes rasgos

┌───────────────┐ ▲
│ Controllers │ ← Spring Web (REST, auth)
└──────┬────────┘ │
│DTOs / RequestMap │
┌──────▼────────┐ │
│ Services │ ← Lógica de negocio, JWT, fraude
└──────┬────────┘ │
│JPA Entities │
┌──────▼────────┐ │
│ Repositories │ ← Spring Data JPA
└──────┬────────┘ │
│ │
┌──────▼────────┐ │
│ MySQL + Flyway│ ← Persistencia, migraciones
└───────────────┘

---
## Setup
```bash
# 1. Clona el repo
git clone https://github.com/979-Pao/Bank-Back.git
cd BankBack

# 2. Configura variables (application-dev.properties)
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:8080/bankback
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

---

## Controladores y rutas

| Método |  Rol    | Endpoint                      |Descripción                                       |
|--------|------|-------------------------------|---------------------------------------------------|
|  POST    |Auth   | `/auth/login`                 |Iniciar sesión y recibir un JWT Válido            |
|  POST    |Admin  | `/admin/checking`             |Crea cuenta corriente o student                   |
|  POST    | Admin  | `/admin/savings`              |Crea cuenta de ahorro                             |
|  POST    |Admin  | `/admin/credit-card`          | tarjeta de crédito                              |
|  POST    |Admin  | `/admin/holders`              | Crea un titular de cuenta (AccountHolder)            |
|  POST    | Admin  | `/admin/third-party`          |Crea una pasarela de pago (Thirdparty)               |
|  GET    |Admin  | `/admin/accounts`             |  Lista todas las cuentas                             |
|  GET    |Admin  | `/admin/holders`              | Lista todos los titulares (AccountHolder)            |
|  GET    |Admin  | `/admin/third-parties`        | Lista todos los usuarios (Thirdparty)                |
| PUT     |Admin  | `/admin/accounts/{id}/balance` | Actualizar el saldo de una cuenta especifica         |
| DELETE  |Admin  | `/admin/accounts/{id}`        |  Cierra la cuenta por su ID                          |
|  POST      |Holder  | `/holder/transfer`            | Transferencia a otra cuenta                          |
|   GET    |Holder    | `/holder/accounts`            | Lista todas mis cuentas                              |
|  GET      |Holder    | `/holder/account/{id}`        | Detalle de cuenta                                    |
|   PATCH     |Holder  | `/holder/{id}/status`         | Cambia estado de la cuenta                           |
|  DELETE      |Holder | `/holder/{id}`                |  Cierra la cuenta                                     |
| POST       |ThirdParty   | `/thirdparty/refund`          | Recibe dinero en una cuenta usando hashKey           |
|  POST      |ThirdParty   | `/thirdparty/payment`         | Envia dinero desde una cuenta usando hashKey         |
|  GET     |ThirdParty    | `/thirdparty/transactions`    | Consulta todas las transaciones asociadas al hashKey |
|  DELETE       |ThirdParty | `/thirdparty/{id}`          | Elimina un usuario ThirdParty                        |
---
## Pruebas unitarias (`/src/test/java/com/System/BankBack/service`)

### `AccountServiceTest.java`
| Método                     | Comprueba                                                                 |
|---------------------------|----------------------------------------------------------------------------------|
| `transfer_ok()`           | Transferencia exitosa entre cuentas activas, con evaluación de fraude incluida. |
| `transfer_frozenRejected()` | Impide transferencia si la cuenta de origen está congelada.                     |

### `AccountServiceUnitTest.java`
| Método                     | Comprueba                                                                  |
|---------------------------|----------------------------------------------------------------------------------|
| `transfer_ok()`           | Transferencia exitosa entre cuentas simuladas. Verifica saldos y persistencia.  |
| `transfer_insufficient()` | Lanza excepción si no hay fondos suficientes.                                   |

### `FraudDetectionServiceTest.java`
| Método                   | Comprueba                                                                             |
|--------------------------|---------------------------------------------------------------------------------------------|
| `freezeOnDailySpike()`   | Congela cuenta si el total diario supera el 150 % del récord histórico.                    |
| `freezeOnBurst()`        | Congela cuenta si hay más de 3 movimientos en menos de 1 segundo.                          |
| `safeWhenNormal()`       | No congela la cuenta si las condiciones de operación son normales.                         |

### `ThirdPartyServiceTest.java`
| Método         | Comprueba                                                                      |
|----------------|--------------------------------------------------------------------------------------|
| `send_ok()`    | Que un usuario ThirdParty pueda enviar dinero exitosamente usando un `hashedKey`.   |

---

## Pruebas de integración (`/src/test/java/com/System/BankBack/web`)

###  `AdminFlowIT.java`
| Método                                | Comprueba                                                                  |
|---------------------------------------|----------------------------------------------------------------------------------|
| `admin_can_create_checking_and_get_201()` | Login de Admin y creación de cuenta Checking con token JWT.                  |

### `AccountControllerIT.java`
| Método             | Comprueba                                                                |
|--------------------|--------------------------------------------------------------------------------|
| `transfer_success()` | Transferencia HTTP real (con token) entre dos cuentas del mismo titular.     |

### `ThirdPartyControllerIT.java`
| Método         | Comprueba                                                                       |
|----------------|---------------------------------------------------------------------------------------|
| `sendMoney_ok()` | Que un tercero autorizado pueda enviar dinero con clave y token válidos.           |

### `ThirdPartyFilterIT.java`
| Método             | Comprueba                                                 |
|--------------------|----------------------------------------------------------------|
| `badKey_returns401()` | Rechazo (401 Unauthorized) si el `X-Hashed-Key` es inválido.  |

---

## Repositorios (`/repo`)

### `RepositoriesIT.java`
| Método               | Comprueba                                              |
|----------------------|--------------------------------------------------------------|
| `findByUsername_ok()` | Verifica que el repositorio devuelva un usuario existente. |

---

## Spring Boot Contexto

### `BankBackApplicationIT.java`
| Método                            | Comprueba                                                       |
|-----------------------------------|-----------------------------------------------------------------------|
| `contextStarts_and_coreBeansPresent()` | Que el contexto Spring arranca correctamente y registra los beans esenciales. |



---
## Enlaces extra
- **[Presentación Bank-Back](https://acrobat.adobe.com/id/urn:aaid:sc:VA6C2:64bf289d-a7c9-4429-bb8c-6b4ab3284d9c)**

---

## Future Work
- 💳 Apple/Google Pay integration
- 📈 Reporte financiero descargable en PDF

---

## Resources
- Start.spring.io
- JWT playground: jwt.io
- Diagramas UML: dbdiagram.io

---

## Author

Paola Montaño 
GitHub → @979‑Pao
