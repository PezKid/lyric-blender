# Spotify Lyrics Generator

A full-stack web application that uses Spotify API and OpenAI to generate song lyrics based on artist styles and genres.

## Tech Stack
- **Backend**: Java Spring Boot, Spring Security, OAuth2
- **Frontend**: React TypeScript
- **APIs**: Spotify Web API, OpenAI GPT API
- **Database**: H2 (development), PostgreSQL (production ready)

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 16+

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

### Environment Variables
Create a `.env` file in the root directory:
```
OPENAI_API_KEY=your_openai_key
SPOTIFY_CLIENT_ID=your_spotify_client_id
SPOTIFY_CLIENT_SECRET=your_spotify_client_secret
```

## Features (Planned)
- [ ] Spotify OAuth authentication
- [ ] Fetch user listening history
- [ ] AI-generated lyrics in artist styles
- [ ] Genre blending capabilities
- [ ] Real-time lyric generation
- [ ] Responsive web interface

## Development Status
🚧 **In Development** - Basic project structure complete, working on API integrations.

## Project Goals
Built as a demonstration of full-stack development capabilities, API integration, and modern web technologies for software engineering opportunities.
