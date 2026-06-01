# Food Ordering Platform

A full-stack food ordering web application with customer, branch manager, and admin roles. Built with Spring Boot and Vue 3, integrated with VNPay payment gateway, an AI chatbot powered by Groq (LLaMA 3.3), and an interactive branch locator map.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | Vue 3, TypeScript, Vite, Tailwind CSS |
| Backend | Spring Boot 3, Java 17, Spring Security |
| Database | PostgreSQL 16 |
| Auth | JWT (HTTP-only cookies) |
| Payments | VNPay |
| AI Chatbot | Groq API (LLaMA 3.3-70b) |
| Maps | Leaflet + OpenStreetMap |
| DevOps | Docker, Docker Compose |

## Features

- **Customer** — browse menu, place orders, track order history, manage addresses, pay via COD or VNPay
- **Branch Manager** — view and update order statuses for their branch
- **Admin** — manage products, categories, branches, and users
- **AI Chatbot** — customer support chatbot with live menu context
- **Interactive Map** — find nearest branch with geocoding

## Prerequisites

- [Docker](https://www.docker.com/) and Docker Compose
- A [Groq API key](https://console.groq.com) (free tier is enough)
- A [Cloudinary](https://cloudinary.com) account (for product image uploads)
- VNPay sandbox credentials (optional — only needed to test payments)

## Getting Started

**1. Clone the repo**

```bash
git clone <your-repo-url>
cd KFCClone
```

**2. Set up environment variables**

```bash
cp .env.example .env
```

Then open `.env` and fill in your values:

```env
POSTGRES_PASSWORD=your_password_here
JWT_SECRET=a_long_random_string_at_least_32_chars

GROQ_API_KEY=your_groq_api_key

VITE_CLOUDINARY_CLOUD_NAME=your_cloud_name
VITE_CLOUDINARY_UPLOAD_PRESET=your_upload_preset

# VNPay sandbox (optional)
VNPAY_TMN_CODE=your_tmn_code
VNPAY_HASH_SECRET=your_hash_secret
```

**3. Start everything**

```bash
docker compose up --build
```

This starts three services:
- PostgreSQL on port `5433`
- Spring Boot API on port `8080`
- Vue frontend on port `5173`

The database schema is applied automatically on first run.

**4. Open the app**

```
http://localhost:5173
```

## Default Accounts

After the schema runs you can create accounts via the register page. To set up an admin, manually update the role in the database:

```sql
UPDATE users SET role = 'admin' WHERE email = 'your@email.com';
```

## Running Tests

```bash
cd backend
mvn test
```

## Project Structure

```
KFCClone/
├── backend/          # Spring Boot API
│   └── src/main/java/com/example/backend/
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── model/
│       ├── dto/
│       └── enums/
├── frontend/         # Vue 3 SPA
│   └── src/
│       ├── pages/
│       ├── components/
│       ├── stores/
│       └── api/
├── db/
│   └── schema.sql    # Database schema
└── docker-compose.yml
```

## Environment Variables Reference

| Variable | Description |
|----------|-------------|
| `POSTGRES_DB` | Database name |
| `POSTGRES_USER` | Database user |
| `POSTGRES_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for signing JWT tokens (min 32 chars) |
| `GROQ_API_KEY` | Groq API key for the AI chatbot |
| `VITE_CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name for image uploads |
| `VITE_CLOUDINARY_UPLOAD_PRESET` | Cloudinary unsigned upload preset |
| `VNPAY_TMN_CODE` | VNPay terminal code |
| `VNPAY_HASH_SECRET` | VNPay HMAC secret |
| `VNPAY_URL` | VNPay payment URL (use sandbox for testing) |
| `VNPAY_RETURN_URL` | URL VNPay redirects to after payment |
