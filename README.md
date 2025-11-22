# CSYE6200 — Concepts of Object Oriented Design

Northeastern University — College of Engineering

Professor: Daniel Peters

---

## Overview

This repository contains a Spring Boot backend and a React + TypeScript frontend scaffolded with Vite. It is used for course assignments and projects for CSYE6200.

---

## Requirements

- Java 11+ (for the backend)
- Node.js 16+ (for the frontend; use the version specified in `frontend/package.json` if present)
- Git and GitHub account
- Recommended IDE: IntelliJ IDEA, Eclipse, or VS Code

If you use Eclipse, ensure you have the Git CLI or GitHub Desktop available to commit and push changes.

---

## Setup & Run (quick)

Backend (Spring Boot / Maven):

```bash
# from repository root
cd backend
# use the included wrapper
./mvnw spring-boot:run
```

To build a runnable JAR:

```bash
cd backend
./mvnw clean package
java -jar target/*.jar
```

Frontend (React + Vite):

```bash
# from repository root
cd frontend
npm install        # or pnpm install / yarn
npm run dev        # start dev server (Vite)
npm run build      # build production bundle
```

---

## Development & Submission Notes

- Commit code to the `main` branch per course instructions.
- Ensure GitHub Actions (CI) pass after pushing; failing checks may affect grading.
- Keep sensitive values out of the repo — use `.env` files locally and do not commit them.

If you need help with GitHub or submissions, contact your TA.

---

## Frontend (brief)

The frontend is a React + TypeScript project using Vite for fast dev rebuilds. The project includes basic ESLint configuration. For production-ready linting, consider enabling type-aware linting rules and the official React/Vite plugins — see Vite and React docs for recommended plugins and rules.

Useful links:

- Vite: https://vitejs.dev/
- React: https://reactjs.org/

---

## References

- Cloning a repository: https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository
