# **StoryScape — Full-Stack Content & Subscription Platform**

**CSYE6200 — Concepts of Object-Oriented Design**
Northeastern University — College of Engineering
Professor: Daniel Peters

---

## **Overview**

StoryScape is a full-stack platform where users can **create, publish, and read content** while browsing a **personalized feed** driven by tags, user interests, connections, and paid visibility features.

On the business side, StoryScape includes a **complete product catalog**, **tier-based configurations**, **subscription management**, and a **billing engine** that generates **monthly invoices** for purchased products such as premium visibility, advertisement slots, and private channels.

This repository contains:

* **Spring Boot backend**
* **React + TypeScript frontend (Vite)**
* **MySQL database schema**

The project demonstrates clean **Object-Oriented Design**, modular architecture, and real-world features such as JWT authentication, configurable pricing, and automated billing.

---

## **Key Features**

### **User Authentication**

* Register users with encrypted passwords
* JWT-based login
* Role/tier-based access (normal, core, other)

### **Content Management**

* Create, edit, delete blog posts
* Tagging system (`tags`, `content_tags`)
* Personalized feed using `feed_records`

### **Product & Subscription System**

* Product catalog (`product_catalog`)
* Tier-based pricing via `product_configurations`
* Promotions & discounts
* User purchases stored in `subscriptions`

### **Billing Engine**

* Monthly cron job to generate invoices
* Invoice + invoice line items stored in DB
* Email notifications on invoice generation

### **Notifications**

* System-generated messages for posts, purchases, promotions

---

## **Tech Stack**

### **Backend**

* Java 17+
* Spring Boot
* Spring Data JPA
* Spring Security (JWT)
* MySQL
* Maven

### **Frontend**

* React + TypeScript
* Vite
* Axios
* React Router DOM

---

## **Requirements**

* Java 17+
* Node.js 16+
* MySQL 8+
* Git / GitHub
* Preferred IDE: IntelliJ or VS Code

---

## **Setup & Run**

### **Backend — Spring Boot**

From repository root:

```bash
cd backend
./mvnw spring-boot:run
```

To build a standalone JAR:

```bash
./mvnw clean package
java -jar target/*.jar
```

---

### **Frontend — React + Vite**

```bash
cd frontend
npm install
npm run dev     # start dev server
npm run build   # create production bundle
```

---

## **Folder Structure**

```
backend/
  src/main/java/… (controllers, services, repositories, entities)
  src/main/resources/
frontend/
  src/pages/
  src/components/
  src/services/
```

---

## **Development Notes**

* Push all work to the **main** branch per course rules.
* Do not commit `.env` files or database credentials.
* Run both backend and frontend concurrently during local development.
* JWT tokens are generated after login and stored client-side.

---

## **Object-Oriented Design Used in This Project**

* **Abstraction:** Separate modules for content, billing, products, and authentication
* **Encapsulation:** Entities hide internal fields through getters/setters
* **Inheritance:** Shared entity properties (timestamps, ids) modeled via base classes
* **Polymorphism:** Different product types follow a unified interface
* **Singleton Pattern:** Central Config/Pricing managers
* **Factory Pattern:** ProductFactory for creating product instances based on type
* **SOLID principles** throughout architecture

---

## **Useful Links**

* React: [https://react.dev/](https://react.dev/)
* Vite: [https://vitejs.dev/](https://vitejs.dev/)
* Spring Boot Docs: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
* MySQL Docs: [https://dev.mysql.com/doc/](https://dev.mysql.com/doc/)
