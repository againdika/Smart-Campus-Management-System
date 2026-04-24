# Smart Campus Management System

A Java-based campus resource management application. The system manages room bookings, maintenance requests, and user notifications across three user roles — Administrator, Staff, and Student.

---

## Project Structure

```
SmartCampusSystem/
|
|-- MainApplication.java                  Entry point, menu-driven UI
|
|-- enums/
|   |-- BookingStatus.java                PENDING, CONFIRMED, CANCELLED, COMPLETED
|   |-- Equipment.java                    PROJECTOR, WHITEBOARD, SMART_BOARD, etc.
|   |-- MaintenanceStatus.java            PENDING, ASSIGNED, IN_PROGRESS, COMPLETED
|   |-- Urgency.java                      LOW, MEDIUM, HIGH, CRITICAL
|   |-- UserType.java                     ADMINISTRATOR, STAFF, STUDENT
|
|-- exceptions/
|   |-- BookingException.java             Custom domain exception
|
|-- models/
|   |-- Booking.java                      Booking entity
|   |-- MaintenanceRequest.java           Maintenance request entity
|   |-- Notification.java                 Notification entity
|   |-- Room.java                         Room entity with clone support
|   |-- User.java                         User entity, base for all roles
|
|-- patterns/
|   |-- builder/
|   |   |-- BookingRequestBuilder.java    Builder pattern
|   |
|   |-- facade/
|   |   |-- CampusManagementFacade.java   Facade pattern
|   |
|   |-- factory/
|   |   |-- RoomFactory.java              Factory pattern
|   |
|   |-- observer/
|   |   |-- Observer.java                 Observer interface
|   |   |-- Subject.java                  Subject interface
|   |   |-- UserObserver.java             Concrete observer
|   |
|   |-- singleton/
|       |-- NotificationManager.java      Singleton pattern
|
|-- test/
    |-- SCMSTest.java                     JUnit 4 test cases
```

---

## System Requirements

- Java JDK 1.8 or above
- NetBeans IDE 8.1 or above
- JUnit 4.12

---

## User Roles

| Role | Access Level |
|---|---|
| Administrator | Full system access — manages rooms, bookings, maintenance, and notifications |
| Staff | Can book rooms, report maintenance issues, and view notifications |
| Student | Can browse available rooms, request bookings, and view announcements |

---

## Default Login Credentials

| Role | Username | Password |
|---|---|---|
| Administrator | admin | admin123 |
| Staff | staff1 | staff123 |
| Student | student1 | student123 |

---

## Design Patterns Used

| Pattern | Category | Class |
|---|---|---|
| Factory | Creational | RoomFactory.java |
| Builder | Creational | BookingRequestBuilder.java |
| Singleton | Creational | NotificationManager.java |
| Facade | Structural | CampusManagementFacade.java |
| Observer | Behavioural | Observer.java, UserObserver.java |

---

## Room Types Available

| Room ID | Room Name | Type | Capacity |
|---|---|---|---|
| R001 | Main Lecture Hall | Lecture Hall | 200 |
| R002 | Computer Lab A | Computer Lab | 50 |
| R003 | Conference Room | Conference Room | 30 |
| R004 | Smart Classroom | Smart Classroom | 60 |

---

## How to Run

1. Open the project in NetBeans IDE
2. Right click on the project and select Clean and Build
3. Right click on MainApplication.java and select Run File
4. Log in using any of the default credentials above
5. Navigate the menu based on your user role

---

## How to Run Tests

1. Open SCMSTest.java inside the test package
2. Right click on the file and select Test File
3. View results in the Test Results panel at the bottom of NetBeans
4. 17 tests are expected to pass
5. 1 test (testBookingPastDate) is intentionally designed to fail
##there having an issue when running
---

## Key Features

- Role-based access control for all system operations
- Room booking with conflict detection
- Maintenance request reporting, assignment, and status tracking
- Automatic notifications using the Observer pattern
- Analytics dashboard for Administrators
- Recurring booking support

---

## Author

Aruna Indika | 2025-2026
