# ParkSpot — Smart Society Visitor Parking Management System

[![Spring Boot 3.2.5](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![MySQL 8.0](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Vite / Modern UI](https://img.shields.io/badge/Frontend-Modern%20SPA%20%7C%20Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)

A modern, full-stack, enterprise-grade society visitor parking management system built for residential apartment complexes and gated societies. Enables security personnel and facility managers to log visitor vehicles into numbered bays, track real-time occupancy, release bays on checkout, query visitor audit histories, and manage registered flats.

---

## 🌟 Key Features

- **Executive Analytics Dashboard**: Live metrics for Total Slots, Available Slots, Occupied Slots, Today's Visitors, All-Time Visitors, and Registered Flats. Features visual occupancy gauges and visitor arrival timelines.
- **Interactive Parking Bay Grid**: Color-coded bay cards (Emerald Green for Available, Crimson for Occupied). Occupied bays display real-time Indian license plate badges (`[IND] ...`), visitor details, flat chips, and instant one-click exit modal triggers. Available bays feature one-click quick allocation.
- **Visitor Management Table**: Complete search, filter, and sorting suite for active and checked-out visitors. Live status indicators, entry/exit timestamp auditing, and inline checkout triggers.
- **Flats Directory**: Resident registration records with search and direct visitor allocation for selected flats.
- **Daily Audit & Reporting**: Historical visitor log reports filtered by date with print/PDF export readiness.
- **Resilient Modal Engine & Real-Time Toasts**: Dynamic dropdowns populated directly from live backend APIs, Bean Validation error feedback, and duplicate slot prevention warnings.

---

## 🛠️ Technology Architecture

### Backend
- **Framework**: Spring Boot 3.2.5
- **Language**: Java 17 / 21
- **Persistence**: Spring Data JPA & Hibernate 6
- **Database Profiles**:
  - `default`: In-memory H2 database with auto-schema generation and console (`jdbc:h2:mem:parkspot`)
  - `mysql`: Production MySQL database with connection pooling (`application-mysql.properties`)
- **API Documentation**: Springdoc OpenAPI / Swagger UI (v2.5.0)
- **Validation**: Jakarta Bean Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@Positive`)

### Frontend
- **Design System**: Premium dark navy theme (`#070d18` / `#0b1325`), crisp typography (`Plus Jakarta Sans` & `JetBrains Mono`), glassmorphic cards, emerald green & crimson status badges.
- **Icons**: Custom inline SVG icon set matching Lucide icons (zero broken third-party font dependencies).
- **Tooling**: Vite 5 build toolchain with optional zero-build standalone runtime support.

---

## 🚀 Step-by-Step Execution Guide

### Option A: Standard Full-Stack Startup (Backend + Vite Frontend)

#### 1. Start the Spring Boot Backend

**With MySQL profile:**
```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=mysql"
```

**With In-Memory H2 (Quick Demo / No MySQL setup required):**
```powershell
.\mvnw.cmd spring-boot:run
```

*The backend starts at `http://localhost:8080` and pre-seeds 20 parking bays and 8 residential flats.*

#### 2. Start the Frontend (Vite)
Open a new terminal window in the `frontend` folder:
```powershell
cd frontend
npm install
npm run dev
```
Open **`http://localhost:5173`** in your browser.

---

### Option B: Built-in Single Server Startup (College Demo Friendly)

The redesigned frontend is also embedded inside Spring Boot at `src/main/resources/static/index.html`.

1. Start the Spring Boot backend:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
2. Open your browser directly to:
   **`http://localhost:8080/`**

No Node.js or second terminal required! The entire UI runs seamlessly from the Spring Boot web container.

---

## 📡 REST API Reference

### Parking & Visitor Operations (`/api/parking`)

| Method | Endpoint | Description | Frontend Page |
|---|---|---|---|
| `GET` | `/api/parking/dashboard` | Aggregated dashboard summary counts | Dashboard |
| `GET` | `/api/parking/slots` | All slot statuses (occupancy, vehicle, flat) | Parking Slots, Entry Modal |
| `GET` | `/api/parking/slots/available` | Only available parking bays | Entry Modal |
| `GET` | `/api/parking/slots/occupied` | Only occupied parking bays | Parking Slots (Filter) |
| `POST` | `/api/parking/entry` | Log visitor entry and allocate slot | "+ Log Visitor Entry" Modal |
| `PUT` | `/api/parking/exit/{vehicleId}` | Mark vehicle exit and free slot | Exit Confirmation Modal |
| `GET` | `/api/parking/report/daily?date=YYYY-MM-DD` | Daily visitor audit report | Daily Report Page |
| `GET` | `/api/parking/search?vehicleNumber={query}` | Search active/historical visitors by plate | Visitors Page (Search bar) |
| `GET` | `/api/parking/by-flat/{flatNumber}` | Filter visitor logs by flat number | Flats Page |

### Flat Management (`/api/flats`)

| Method | Endpoint | Description | Frontend Page |
|---|---|---|---|
| `GET` | `/api/flats` | List all registered society flats | Flats Page, Entry Modal |
| `POST` | `/api/flats` | Register a new flat (`flatNumber`, `ownerName`, `contactNumber`) | "+ Register Flat" Modal |
| `GET` | `/api/flats/{id}` | Retrieve flat by database ID | Flats Details |
| `PUT` | `/api/flats/{id}` | Update existing flat details | Flats Management |
| `DELETE` | `/api/flats/{id}` | Remove a flat | Flats Management |

### Slot Management (`/api/slots`)

| Method | Endpoint | Description | Frontend Page |
|---|---|---|---|
| `GET` | `/api/slots` | List all physical parking slots | Parking Slots |
| `POST` | `/api/slots` | Create a new parking bay (`slotNumber`) | "+ Add Slot Bay" Modal |
| `DELETE` | `/api/slots/{id}` | Delete a parking slot | Slot Management |

---

## 🔒 Business Rules Enforced

1. **Rule #1: Slot Double-Booking Prevention**
   Attempting to assign an occupied slot returns `HTTP 409 Conflict` with error message `"Slot #X is already occupied"`.
2. **Rule #2: Exit Idempotency & Validation**
   Attempting to check out an already-exited vehicle returns `HTTP 400 Bad Request` with `"Vehicle has already exited at <timestamp>"`.
3. **Rule #3: Foreign Key Integrity**
   Entries require valid existing flat and slot records in the database.
4. **Rule #4: Automatic Status Lifecycle**
   Logging an entry sets `ParkingSlot.occupied = true`. Marking an exit immediately sets `ParkingSlot.occupied = false` and timestamps `exitTime`.

---

## 🧪 Testing and Verification

Run the full automated test suite:
```powershell
.\mvnw.cmd test
```

Verified Test Results:
- `ParkingServiceTest.testLogEntry_Success` — PASSED
- `ParkingServiceTest.testLogEntry_SlotAlreadyOccupied_ThrowsException` — PASSED
- `ParkingServiceTest.testMarkExit_AlreadyExited_ThrowsException` — PASSED
- `ParkingServiceTest.testSlotReuseAfterExit` — PASSED
- `ParkingServiceTest.testLogEntry_NonExistentFlat` — PASSED
- `ParkingServiceTest.testLogEntry_NonExistentSlot` — PASSED
- `ParkingServiceTest.testDailyReport` — PASSED
- `ParkingServiceTest.contextLoads` — PASSED
- **Total Tests: 8, Failures: 0, Errors: 0**
