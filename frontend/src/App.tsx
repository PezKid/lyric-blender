import React, { useState, useEffect } from 'react';

interface SpotifyProfile {
  display_name: string;
}

interface SpotifyArtist {
  name: string;
  genres?: string[];
}

interface GeneratedLyrics {
  choices: Array<{
    message: {
      content: string;
    }
  }>;
}

type AppState = 'loading' | 'login' | 'form' | 'generating' | 'results';

const App = () => {
  const [appState, setAppState] = useState<AppState>('loading');
  const [error, setError] = useState<string | null>(null);

  // Spotify data
  const [spotifyProfile, setSpotifyProfile] = useState<SpotifyProfile | null>(null);
  const [topArtists, setTopArtists] = useState<SpotifyArtist[]>([]);
  
  // Form data
  const [artist1, setArtist1] = useState<string>('');
  const [artist2, setArtist2] = useState<string>('');
  const [genre, setGenre] = useState<string>('');
  const [theme, setTheme] = useState<string>('');
  
  // Generated lyrics
  const [generatedLyrics, setGeneratedLyrics] = useState<string>('');

  // Extract unique genres from top artists
  const getTopGenres = () => {
    const allGenres = topArtists.flatMap(artist => artist.genres || []);
    const uniqueGenres = Array.from(new Set(allGenres));
    return uniqueGenres.slice(0, 20);
  };

  // Fetch user data on mount
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const profileResponse = await fetch('http://127.0.0.1:8080/api/spotify/profile', {
          method: 'GET',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (profileResponse.status === 401) {
          setAppState('login');
          return;
        }

        if (!profileResponse.ok) {
          throw new Error('Failed to fetch user profile.');
        }

        const profileData = await profileResponse.json();
        setSpotifyProfile(profileData);

        // Fetch top artists
        const artistsResponse = await fetch('http://127.0.0.1:8080/api/spotify/top-artists', {
          method: 'GET',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
          },
        });
        
        if (!artistsResponse.ok) throw new Error("Failed to fetch top artists.");
        const artistsData = await artistsResponse.json();
        
        if (artistsData && artistsData.items) {
          setTopArtists(artistsData.items.slice(0, 20)); 
          setAppState('form');
        }

      } catch (err: any) {
        console.error('Network or fetch error:', err);
        setAppState('login');
      }
    };

    fetchUserData();
  }, []);

  // Handle lyrics generation
  const handleGenerateLyrics = async () => {
    if (!artist1 || !artist2 || !theme.trim()) {
      setError('Please fill in all fields');
      return;
    }

    // Check theme word limit (10 words max)
    const wordCount = theme.trim().split(/\s+/).length;
    if (wordCount > 10) {
      setError('Theme must be 10 words or less');
      return;
    }

    setAppState('generating');
    setError(null);

    try {
      const response = await fetch('http://127.0.0.1:8080/api/lyrics/blend', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          artist1: artist1,
          artist2: artist2,
          genre: genre,
          theme: theme.trim()
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to generate lyrics');
      }

      const data: GeneratedLyrics = await response.json();
      const lyrics = data.choices[0]?.message?.content || 'No lyrics generated';
      setGeneratedLyrics(lyrics);
      setAppState('results');

    } catch (err: any) {
      console.error('Error generating lyrics:', err);
      setError('Failed to generate lyrics. Please try again.');
      setAppState('form');
    }
  };

  // Reset to form
  const handleTryAgain = () => {
    setGeneratedLyrics('');
    setArtist1('');
    setArtist2('');
    setGenre('');
    setTheme('');
    setError(null);
    setAppState('form');
  };

  // Render different states
  const renderContent = () => {
    switch (appState) {
      case 'loading':
        return (
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-400 mx-auto mb-4"></div>
            <p className="text-lg text-gray-400">Loading your music data...</p>
          </div>
        );

      case 'login':
        return (
          <div className="text-center">
            <p className="text-lg text-gray-400 mb-8">
              Login with Spotify to blend your favorite artists and create unique lyrics.
            </p>
            <a
              href="http://127.0.0.1:8080/oauth2/authorization/spotify"
              className="bg-green-500 hover:bg-green-600 text-white font-bold py-3 px-6 rounded-full transition duration-300 transform hover:scale-105 inline-flex items-center"
            >
              Login with Spotify
            </a>
            {error && (
              <div className="mt-8 p-4 bg-red-800 rounded-lg text-sm text-red-200">
                <p>{error}</p>
              </div>
            )}
          </div>
        );

      case 'form':
        const topGenres = getTopGenres();
        return (
          <div>
            {/* Welcome message */}
            <div className="text-center mb-8">
              <h2 className="text-2xl font-bold text-green-400 mb-2">
                Welcome, {spotifyProfile?.display_name}!
              </h2>
              <p className="text-gray-400">
                Blend two of your top artists to create unique lyrics
              </p>
            </div>

            {/* Form */}
            <div className="space-y-6">
              {/* Artist 1 Dropdown */}
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">
                  First Artist
                </label>
                <select
                  value={artist1}
                  onChange={(e) => setArtist1(e.target.value)}
                  className="w-full p-3 bg-gray-700 border border-gray-600 rounded-lg text-white focus:ring-2 focus:ring-green-400 focus:border-transparent"
                >
                  <option value="">Select your first artist</option>
                  {topArtists.map((artist, index) => (
                    <option key={index} value={artist.name}>
                      {artist.name}
                    </option>
                  ))}
                </select>
              </div>

              {/* Artist 2 Dropdown */}
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">
                  Second Artist
                </label>
                <select
                  value={artist2}
                  onChange={(e) => setArtist2(e.target.value)}
                  className="w-full p-3 bg-gray-700 border border-gray-600 rounded-lg text-white focus:ring-2 focus:ring-green-400 focus:border-transparent"
                >
                  <option value="">Select your second artist</option>
                  {topArtists
                    .filter(artist => artist.name !== artist1) // Don't allow same artist twice
                    .map((artist, index) => (
                      <option key={index} value={artist.name}>
                        {artist.name}
                      </option>
                    ))}
                </select>
              </div>

              {/* Genre Dropdown */}
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">
                  Genre Influence
                </label>
                <select
                  value={genre}
                  onChange={(e) => setGenre(e.target.value)}
                  className="w-full p-3 bg-gray-700 border border-gray-600 rounded-lg text-white focus:ring-2 focus:ring-green-400 focus:border-transparent"
                >
                  <option value="">Select a genre</option>
                  {topGenres.map((genreName, index) => (
                    <option key={index} value={genreName}>
                      {genreName}
                    </option>
                  ))}
                </select>
              </div>

              {/* Theme Input */}
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">
                  Song Theme
                </label>
                <input
                  type="text"
                  value={theme}
                  onChange={(e) => setTheme(e.target.value)}
                  placeholder="What should the song be about? (max 10 words)"
                  className="w-full p-3 bg-gray-700 border border-gray-600 rounded-lg text-white focus:ring-2 focus:ring-green-400 focus:border-transparent"
                  maxLength={100}
                />
                <p className="text-xs text-gray-500 mt-1">
                  Words: {theme.trim() ? theme.trim().split(/\s+/).length : 0}/10
                </p>
              </div>

              {/* Generate Button */}
              <button
                onClick={handleGenerateLyrics}
                disabled={!artist1 || !artist2 || !theme.trim()}
                className="w-full bg-green-500 hover:bg-green-600 disabled:bg-gray-600 disabled:cursor-not-allowed text-white font-bold py-3 px-6 rounded-lg transition duration-300 transform hover:scale-105 disabled:transform-none"
              >
                ✨ Generate Lyric Blend ✨
              </button>

              {error && (
                <div className="p-4 bg-red-800 rounded-lg text-sm text-red-200">
                  <p>{error}</p>
                </div>
              )}
            </div>
          </div>
        );

      case 'generating':
        return (
          <div className="text-center">
            <div className="animate-pulse mb-6">
              <div className="text-6xl mb-4">🎼</div>
              <h3 className="text-2xl font-bold text-green-400 mb-2">Creating Your Blend...</h3>
              <p className="text-gray-400">
                Mixing {artist1} and {artist2} with {theme}
              </p>
            </div>
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-400 mx-auto"></div>
          </div>
        );

      case 'results':
        return (
          <div>
            {/* Results Header */}
            <div className="text-center mb-6">
              <h2 className="text-2xl font-bold text-green-400 mb-2">
                Your Blended Lyrics
              </h2>
              <p className="text-gray-400">
                {artist1} and {artist2} as {theme}
              </p>
            </div>

            {/* Lyrics Display */}
            <div className="bg-gray-700 p-6 rounded-lg mb-6">
              <pre className="whitespace-pre-wrap text-gray-200 leading-relaxed font-mono text-sm">
                {generatedLyrics}
              </pre>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-4">
              <button
                onClick={handleTryAgain}
                className="flex-1 bg-gray-600 hover:bg-gray-500 text-white font-bold py-3 px-6 rounded-lg transition duration-300"
              >
                Back to Home
              </button>
              <button
                onClick={handleGenerateLyrics}
                className="flex-1 bg-green-500 hover:bg-green-600 text-white font-bold py-3 px-6 rounded-lg transition duration-300"
              >
                Regenerate
              </button>
            </div>
          </div>
        );

      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-4 font-sans">
      <div className="bg-gray-800 p-8 rounded-xl shadow-lg w-full max-w-2xl">
        {/* Header */}
        <h1 className="text-4xl font-bold mb-8 text-center text-green-400">
          Lyric Blender
        </h1>
        
        {/* Dynamic Content */}
        {renderContent()}
      </div>
    </div>
  );
};

export default App;