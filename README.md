# 📝 JWT Notes App (Spring Boot + Kotlin + MongoDB)

A secure **Notes REST API** built using **Spring Boot**, **Kotlin**, and **MongoDB**, featuring **JWT-based authentication**, **refresh tokens**, and **user-specific note management**.

Each user can create, read, update, and delete their own notes securely.

---

## 🚀 Features

- 🔐 JWT Authentication (Access & Refresh Tokens)
- 👤 Multi-user support (user-specific notes)
- 🛡 Spring Security with custom JWT filter
- 📄 CRUD operations for Notes
- 🗄 MongoDB integration
- ⏳ Refresh token expiry using MongoDB TTL index
- ✅ Validation & custom exception handling
- ⚡ Clean Kotlin codebase

---

## 🧰 Tech Stack

- **Backend:** Spring Boot
- **Language:** Kotlin
- **Database:** MongoDB
- **Security:** Spring Security + JWT
- **Build Tool:** Gradle (Kotlin DSL)
- **Java Version:** 17

---

## 📂 Project Structure
src/main/kotlin
├── data
│ ├── modal
│ ├── security
│
├── domain
│ └── repo
├── presentaction
│ ├── controller
│ └── exception
| └── mapper
└── JwtNoteApplication.kt

## 🛡 Security Highlights

- Stateless authentication
- Custom `OncePerRequestFilter`
- Secure token validation
- Per-request user resolution using `@AuthenticationPrincipal`
- Prevents cross-user data access

---

## ⚙️ Configuration

### application.properties

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/jwt_notes
spring.data.mongodb.auto-index-creation=true
API_KEY=your_api_key_here

🙌 Author
Mohamed Asraf Ali
Spring Boot & Android Developer
