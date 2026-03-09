const fs = require('fs');
const readline = require('readline');

// Regular expressions
const entryStartRegex = /^([^\t]+)\t/;          // line starting with word + tab
const posRegex = /^\*\s*(.+)/;                  // part of speech marker: "* danh từ", etc.
const pronRegex = /\/([^\/]+)\//;                // pronunciation between slashes
const exampleRegex = /^=/;                        // example lines start with "="

/**
 * Parse the content of a single dictionary entry.
 * @param {string} word - The headword.
 * @param {string} content - The full content after the tab (including newlines).
 * @returns {Object} - { word, meaning, example, part_of_speech, pronunciation }
 */
function parseEntry(word, content) {
  const lines = content.split('\n');
  let partOfSpeech = '';
  let pronunciation = '';
  const meaningLines = [];
  const exampleLines = [];

  for (let line of lines) {
    line = line.trimEnd(); // keep leading spaces if any? we'll trim right only

    // Check for part of speech
    if (!partOfSpeech) {
      const posMatch = line.match(posRegex);
      if (posMatch) {
        partOfSpeech = posMatch[1].trim();
        continue; // don't add this line to meaning
      }
    }

    // Check for pronunciation (if not already found)
    if (!pronunciation) {
      const pronMatch = line.match(pronRegex);
      if (pronMatch) {
        pronunciation = pronMatch[1].trim();
        // do not skip the line – it may also contain meaning text
      }
    }

    // Check for example
    if (exampleRegex.test(line)) {
      exampleLines.push(line.substring(1).trim()); // remove leading "="
    } else {
      // everything else goes into meaning (including "@" lines, "-" lines, etc.)
      meaningLines.push(line);
    }
  }

  return {
    word,
    meaning: meaningLines.join('\n').trim(),
    example: exampleLines.join('\n').trim(),
    part_of_speech: partOfSpeech,
    pronunciation: pronunciation
  };
}

/**
 * Process the input stream and output JSON array of entries.
 */
async function processInput(inputStream) {
  const rl = readline.createInterface({
    input: inputStream,
    crlfDelay: Infinity
  });

  const entries = [];
  let currentWord = null;
  let currentLines = [];

  for await (const line of rl) {
    const match = line.match(entryStartRegex);
    if (match) {
      // Start of a new entry – save previous one first
      if (currentWord !== null) {
        const content = currentLines.join('\n');
        entries.push(parseEntry(currentWord, content));
      }
      // Begin new entry
      currentWord = match[1];
      // The part after the tab becomes the first line of content
      currentLines = [line.substring(currentWord.length + 1)]; // +1 for tab
    } else {
      // Continuation of current entry (or before first entry)
      if (currentWord !== null) {
        currentLines.push(line);
      }
      // else ignore lines before first entry (shouldn't happen)
    }
  }

  // Last entry
  if (currentWord !== null) {
    const content = currentLines.join('\n');
    entries.push(parseEntry(currentWord, content));
  }

  // Output JSON
  console.log(JSON.stringify(entries, null, 2));
}

// Determine input source: file argument or stdin
const inputFile = process.argv[2];
if (inputFile) {
  const stream = fs.createReadStream(inputFile, 'utf8');
  processInput(stream).catch(console.error);
} else {
  processInput(process.stdin).catch(console.error);
}