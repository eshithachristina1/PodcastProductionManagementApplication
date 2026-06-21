# Podcast Production & Team Collaboration Platform

A full-stack application designed to help podcast teams collaborate throughout the entire podcast production lifecycle. The platform focuses on team collaboration, episode planning, production workflow management, guest management, recording scheduling, asset management, task tracking, and progress monitoring.

> **Note:** This application does NOT host podcasts. Episodes are published and distributed through external platforms such as Spotify, YouTube, Apple Podcasts, Amazon Music, etc.

---

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL
- Maven

### Frontend
- React 18
- Vite 5
- React Router DOM 6
- Axios
- React Toastify
- CSS Modules
- Recharts

---

## Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **MySQL 8** or higher
- **Maven 3.8** or higher

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd PodcastProductionMangementApplication
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE podcast_db;
```

### 3. Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Update database credentials in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/podcast_db
spring.datasource.username=root
spring.datasource.password=password
```

Build and run the backend:

```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`.

The application will automatically create database tables and seed sample data on first run.

### 4. Frontend Setup

Open a new terminal and navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:5173`.

---

## Default Users (Seed Data)

| Name | Email | Password | Role |
|------|-------|----------|------|
| John Admin | admin@podcast.com | admin123 | ADMIN |
| Sarah Host | host@podcast.com | host123 | HOST |
| Mike Producer | producer@podcast.com | producer123 | PRODUCER |
| Emily Writer | writer@podcast.com | writer123 | WRITER |
| David Editor | editor@podcast.com | editor123 | EDITOR |

---

## API Endpoints

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/role/{role}` - Get users by role
- `POST /api/users/login` - Login

### Podcasts
- `GET /api/podcasts` - Get all podcasts
- `GET /api/podcasts/{id}` - Get podcast by ID
- `POST /api/podcasts` - Create podcast
- `PUT /api/podcasts/{id}` - Update podcast
- `DELETE /api/podcasts/{id}` - Delete podcast

### Seasons
- `GET /api/seasons` - Get all seasons
- `GET /api/seasons/{id}` - Get season by ID
- `GET /api/seasons/podcast/{podcastId}` - Get seasons by podcast
- `POST /api/seasons` - Create season
- `PUT /api/seasons/{id}` - Update season
- `DELETE /api/seasons/{id}` - Delete season

### Episodes
- `GET /api/episodes` - Get all episodes
- `GET /api/episodes/{id}` - Get episode by ID
- `GET /api/episodes/season/{seasonId}` - Get episodes by season
- `POST /api/episodes` - Create episode
- `PUT /api/episodes/{id}` - Update episode
- `DELETE /api/episodes/{id}` - Delete episode
- `PATCH /api/episodes/{id}/status` - Change episode status

### Guests
- `GET /api/guests` - Get all guests
- `GET /api/guests/{id}` - Get guest by ID
- `GET /api/guests/episode/{episodeId}` - Get guests by episode
- `POST /api/guests` - Create guest
- `PUT /api/guests/{id}` - Update guest
- `DELETE /api/guests/{id}` - Delete guest

### Tasks
- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `GET /api/tasks/episode/{episodeId}` - Get tasks by episode
- `GET /api/tasks/user/{userId}` - Get tasks by user
- `POST /api/tasks` - Create task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task
- `PUT /api/tasks/{taskId}/assign/{userId}` - Assign task to user

### Recording Sessions
- `GET /api/recording-sessions` - Get all sessions
- `GET /api/recording-sessions/{id}` - Get session by ID
- `GET /api/recording-sessions/episode/{episodeId}` - Get sessions by episode
- `GET /api/recording-sessions/upcoming` - Get upcoming recordings
- `POST /api/recording-sessions` - Create session
- `PUT /api/recording-sessions/{id}` - Update session
- `DELETE /api/recording-sessions/{id}` - Delete session

### Assets
- `GET /api/assets` - Get all assets
- `GET /api/assets/{id}` - Get asset by ID
- `GET /api/assets/episode/{episodeId}` - Get assets by episode
- `GET /api/assets/download/{id}` - Download asset
- `POST /api/assets/upload/{episodeId}` - Upload asset
- `DELETE /api/assets/{id}` - Delete asset

### Notifications
- `GET /api/notifications/user/{userId}` - Get user notifications
- `GET /api/notifications/user/{userId}/unread-count` - Get unread count
- `POST /api/notifications` - Create notification
- `PUT /api/notifications/{id}/read` - Mark as read

### Dashboard
- `GET /api/dashboard` - Get dashboard metrics

---

## Project Structure

### Backend
```
backend/
├── src/main/java/com/podcastmanagement/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── dto/
│   │   ├── request/     # Request DTOs
│   │   └── response/    # Response DTOs
│   ├── entity/          # JPA entities
│   ├── enums/           # Enum classes
│   ├── exception/       # Exception handling
│   ├── repository/      # JPA repositories
│   ├── service/
│   │   └── impl/        # Service implementations
│   └── util/            # Utility classes
├── src/main/resources/
│   └── application.properties
├── uploads/             # File upload directory
└── pom.xml
```

### Frontend
```
frontend/
├── public/
├── src/
│   ├── assets/          # Static assets
│   ├── components/      # Reusable components
│   ├── context/         # React context
│   ├── hooks/           # Custom hooks
│   ├── layouts/         # Layout components
│   ├── pages/           # Page components
│   ├── routes/          # Route configuration
│   ├── services/        # API services
│   ├── styles/          # Global styles
│   ├── App.jsx          # Root component
│   └── main.jsx         # Entry point
├── index.html
├── package.json
└── vite.config.js
```

---

## Episode Workflow

Each episode progresses through these stages:

1. **IDEA** - Episode concept is created
2. **RESEARCH** - Research and topic preparation
3. **SCRIPT_WRITING** - Script and talking points
4. **RECORDING** - Audio/video recording
5. **EDITING** - Post-production editing
6. **REVIEW** - Final review and approval
7. **READY_TO_PUBLISH** - Ready for distribution

---

## Features

- **Dashboard** - Visual analytics with charts and metrics
- **User Management** - Team member administration
- **Podcast Management** - Multi-podcast support with seasons
- **Episode Tracking** - Full production workflow
- **Task Management** - Assign tasks to team members
- **Guest Management** - Contact and episode association
- **Recording Schedule** - Calendar-based session planning
- **Asset Management** - File upload and download system
- **Notifications** - In-app notification system
- **Search & Filter** - Find content quickly
- **Pagination** - Handle large datasets

---

## User Roles

- **ADMIN** - Full system access
- **HOST** - Episode hosting and management
- **PRODUCER** - Production oversight
- **WRITER** - Script and content creation
- **EDITOR** - Post-production editing
- **DESIGNER** - Visual asset creation
