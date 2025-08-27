# Lyric Blend

A full-stack web application that AI generates lyrics combined from your top artists and genres.

## Features
- Spotify OAuth integration for personalized artist data
- Artist blending using your top 10 Spotify artists
- Genre selection from your listening history
- AI-powered lyric generation with OpenAI
- Responsive React frontend with real-time validation
- Spring Boot REST API backend

## Tech Stack
- **Frontend**: React, TypeScript, Tailwind CSS
- **Backend**: Spring Boot, Spring Security OAuth2, WebFlux
- **APIs**: Spotify Web API, OpenAI API
- **Database**: H2 (in-memory for development)

## Setup Instructions

### Prerequisites
- Java 17+
- Node.js 16+
- Spotify Developer Account
- OpenAI API Key

### Environment Variables
Create a `.env` file in the root directory:
```
OPENAI_API_KEY=your_openai_key
SPOTIFY_CLIENT_ID=your_spotify_client_id
SPOTIFY_CLIENT_SECRET=your_spotify_client_secret
```

### Spotify App Configuration
1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Create a new app
3. Set Redirect URI to: `http://127.0.0.1:8080/login/oauth2/code/spotify`
4. Add your email to Users and Access (if in Development Mode)

### Running the Application

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend runs on http://localhost:8080

### Frontend Setup
```bash
cd frontend
npm install
npm start
```
Frontend runs on http://localhost:3000

## Project Goals
Built as a demonstration of full-stack development capabilities, API integration, and modern web technologies for software engineering opportunities.