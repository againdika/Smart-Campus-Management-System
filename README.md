# ðŸ« Smart Campus Management System

A Java-based campus management prototype developed as part of the **CMP 7001 Advanced Programming** module. The system brings together room bookings, maintenance requests, and campus notifications into a single unified platform â€” built around solid object-oriented principles and five design patterns.

---

## ðŸ“‹ Table of Contents

- [Overview](#overview)
- [System Users](#system-users)
- [Features](#features)
- [Project Structure](#project-structure)
- [Design Patterns](#design-patterns)
- [How to Run](#how-to-run)
- [Running the Tests](#running-the-tests)
- [Technologies Used](#technologies-used)

---

## Overview

The Smart Campus Management System (SCMS) is a menu-driven Java console application that allows three types of users â€” Administrator, Staff Member, and Student â€” to interact with campus resources based on their role. The system enforces role-based access control throughout, meaning each user only sees and does what is relevant to them.

---

## System Users

| User | Default Username | Default Password | Access Level |
|---|---|---|---|
| Administrator | `admin` | `admin123` | Full system access |
| Staff Member | `staff1` | `staff123` | Booking + Maintenance |
| Student | `student1` | `student123` | Booking + Announcements |

---

## Features

### ðŸ” Authentication
- Secure login with credential verification
- Role-based menu â€” each user sees only their permitted options

### ðŸ  Room Management *(Admin only)*
- Add, update, and deactivate rooms
- View all rooms or only available ones
- Four room types supported â€” Lecture Hall, Computer Lab, Conference Room, Smart Classroom

### ðŸ“… Booking Management
- Book a room by entering Room ID, date, and time slot
- View existing bookings
- Cancel bookings
- Conflict detection prevents double booking

### ðŸ”§ Maintenance Management
- Report maintenance issues with urgency levels (Low / Medium / High / Critical)
- Admin assigns requests to staff members
- Track status updates â€” Pending â†’ Assigned â†’ In Progress â†’ Completed

### ðŸ”” Notifications
- Automatic notifications triggered on booking, cancellation, and maintenance updates
- View notification history per user

### ðŸ“Š Analytics Dashboard *(Admin only)*
- Most booked rooms
- Active maintenance requests
- Total bookings and room statistics

---

## Project Structure

```
SmartCampusSystem/
â”‚
â”œâ”€â”€ MainApplication.java          â† Entry point â€” menu-driven UI
â”‚
â”œâ”€â”€ enums/
â”‚   â”œâ”€â”€ BookingStatus.java        â† PENDING, CONFIRMED, CANCELLED, COMPLETED
â”‚   â”œâ”€â”€ Equipment.java            â† PROJECTOR, WHITEBOARD, SMART_BOARD, etc.
â”‚   â”œâ”€â”€ MaintenanceStatus.java    â† PENDING, ASSIGNED, IN_PROGRESS, COMPLETED
â”‚   â”œâ”€â”€ Urgency.java              â† LOW, MEDIUM, HIGH, CRITICAL
â”‚   â””â”€â”€ UserType.java             â† ADMINISTRATOR, STAFF, STUDENT
â”‚
â”œâ”€â”€ exceptions/
â”‚   â””â”€â”€ BookingException.java     â† Custom domain exception
â”‚
â”œâ”€â”€ models/
â”‚   â”œâ”€â”€ Booking.java              â† Booking entity
â”‚   â”œâ”€â”€ MaintenanceRequest.java   â† Maintenance request entity
â”‚   â”œâ”€â”€ Notification.java         â† Notification entity
â”‚   â”œâ”€â”€ Room.java                 â† Room entity with clone support
â”‚   â””â”€â”€ User.java                 â† User entity â€” base for all roles
â”‚
â”œâ”€â”€ patterns/
â”‚   â”œâ”€â”€ builder/
â”‚   â”‚   â””â”€â”€ BookingRequestBuilder.java   â† Builder pattern
â”‚   â”œâ”€â”€ facade/
â”‚   â”‚   â””â”€â”€ CampusManagementFacade.java  â† Facade pattern
â”‚   â”œâ”€â”€ factory/
â”‚   â”‚   â””â”€â”€ RoomFactory.java             â† Factory pattern
â”‚   â”œâ”€â”€ observer/
â”‚   â”‚   â”œâ”€â”€ Observer.java                â† Observer interface
â”‚   â”‚   â”œâ”€â”€ Subject.java                 â† Subject interface
â”‚   â”‚   â””â”€â”€ UserObserver.java            â† Concrete observer
â”‚   â””â”€â”€ singleton/
â”‚       â””â”€â”€ NotificationManager.java     â† Singleton pattern
â”‚
â”œâ”€â”€ test/
â”‚   â””â”€â”€ SCMSTest.java             â† JUnit 4 test cases
â”‚
â””â”€â”€ UML/                          â† UML diagrams
```

---

## Design Patterns

| Pattern | Category | Class | Purpose |
|---|---|---|---|
| **Factory** | Creational | `RoomFactory` | Creates pre-configured room types |
| **Builder** | Creational | `BookingRequestBuilder` | Constructs complex Booking objects safely |
| **Singleton** | Creational | `NotificationManager` | Single notification instance across the system |
| **Facade** | Structural | `CampusManagementFacade` | Unified interface to all subsystems |
| **Observer** | Behavioural | `Observer / Subject / UserObserver` | Automatic event-driven notifications |

---

## How to Run

### Prerequisites
- Java JDK 8 or above
- NetBeans IDE 8.1 (or any Java IDE)

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/againdika/Smart-Campus-Management-System.git
```

**2. Open in NetBeans**
- Open NetBeans
- Go to **File â†’ Open Project**
- Select the cloned folder

**3. Build the project**
- Right click the project â†’ **Clean and Build**

**4. Run the application**
- Right click `MainApplication.java` â†’ **Run File**

**5. Login with default credentials**
```
Username: admin
Password: admin123
```

---

## Running the Tests

The project uses **JUnit 4.12** for unit testing.

**In NetBeans:**
- Right click `SCMSTest.java` inside the `test` package
- Select **Test File**
- Results appear in the Test Results panel

### Test Coverage

| Area | Tests | Expected Result |
|---|---|---|
| Login & Authentication | 3 | âœ… Pass |
| Room Booking | 4 | âœ… 3 Pass / âŒ 1 Intentional Fail |
| Booking Cancellation | 2 | âœ… Pass |
| Maintenance Requests | 4 | âœ… Pass |
| Notifications | 2 | âœ… Pass |
| Singleton Pattern | 1 | âœ… Pass |
| **Total** | **18** | **17 Pass / 1 Intentional Fail** |

> âš ï¸ `testBookingPastDate` is intentionally designed to fail. It exposes a known gap in past-date validation within `BookingRequestBuilder` â€” documented openly as part of the test plan.

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 8 | Core development language |
| JUnit 4.12 | Unit testing framework |
| NetBeans 8.1 | Development IDE |
| GitHub | Version control and repository hosting |

---

## Author

**Aruna Indika**
CMP 7001 Advanced Programming â€” MSc Module
