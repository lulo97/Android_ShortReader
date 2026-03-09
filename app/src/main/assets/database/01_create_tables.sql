-- Create books table
CREATE TABLE IF NOT EXISTS books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    word_count INTEGER DEFAULT 0,
    preview TEXT,
    full_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create exercises table
CREATE TABLE IF NOT EXISTS exercises (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    difficulty TEXT DEFAULT 'Beginner',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create word_details table (stores all word meanings and examples)
CREATE TABLE IF NOT EXISTS word_details (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word TEXT NOT NULL UNIQUE,
    meaning TEXT NOT NULL,
    example TEXT,
    part_of_speech TEXT,
    pronunciation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create favourite_words table (now references word_details)
CREATE TABLE IF NOT EXISTS favourite_words (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word_detail_id INTEGER NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (word_detail_id) REFERENCES word_details(id) ON DELETE CASCADE
);

-- Create bookmarks table for user progress
CREATE TABLE IF NOT EXISTS bookmarks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id INTEGER,
    last_position INTEGER DEFAULT 0,
    last_read_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Create index for faster word lookups
CREATE INDEX IF NOT EXISTS idx_word_details_word ON word_details(word);