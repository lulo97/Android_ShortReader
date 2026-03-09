const fs = require('fs');
const path = require('path');

// Configuration – adjust paths if needed
const INPUT_JSON = 'C:/Users/ADMIN/AndroidStudioProjects/ShortReader/app/src/main/assets/database/vocabularies.json';
const OUTPUT_SQL = 'C:/Users/ADMIN/AndroidStudioProjects/ShortReader/app/src/main/assets/database/04_seed_word_details.sql';

// Read JSON file
let rawData;
try {
  rawData = fs.readFileSync(INPUT_JSON, 'utf8');
} catch (err) {
  console.error('Error reading input file:', err.message);
  process.exit(1);
}

// Parse JSON
let dictionary;
try {
  dictionary = JSON.parse(rawData);
} catch (err) {
  console.error('Error parsing JSON:', err.message);
  process.exit(1);
}

const inserts = [];
const escape = (str) => {
  if (str === undefined || str === null) return '';
  return String(str)
    .replace(/\\/g, '\\\\')   // escape backslashes
    .replace(/\r/g, '\\r')    // carriage return
    .replace(/\n/g, '\\n')    // newline
    .replace(/'/g, "''");     // sqlite quote escape
};

console.log('Total top-level keys:', Object.keys(dictionary).length);

for (const groupKey in dictionary) {
  const group = dictionary[groupKey];

  for (const wordKey in group) {
    const entry = group[wordKey];

    if (!entry || typeof entry !== 'object') continue;

    const word = entry.vocabulary || wordKey;
    const ipa = entry.ipa || '';
    const details = entry.details || [];

    const meaningParts = [];
    const exampleParts = [];
    const posSet = new Set();

    details.forEach(detail => {
      const pos = detail.pos || '';
      if (pos) posSet.add(pos);

      const means = detail.means || [];

      means.forEach(m => {
        if (m.mean) meaningParts.push(m.mean);

        if (Array.isArray(m.example)) {
          m.example.forEach(ex => {
            if (ex?.trim()) exampleParts.push(ex.trim());
          });
        }
      });
    });

    const meaning = meaningParts.join('; ');
    const examples = exampleParts.join('\n');
    const partOfSpeech = Array.from(posSet).join(', ');

    const sql =
`INSERT OR IGNORE INTO word_details
(word, meaning, example, part_of_speech, pronunciation)
VALUES ('${escape(word)}','${escape(meaning)}',${examples ? `'${escape(examples)}'` : 'NULL'},'${escape(partOfSpeech)}','${escape(ipa)}');`;

    inserts.push(sql);
  }
}

console.log(inserts.slice(0, 3))

// Write SQL file
try {
  fs.writeFileSync(OUTPUT_SQL, inserts.join('\n'), 'utf8');
  console.log(`✅ Generated ${inserts.length} INSERT statements in ${OUTPUT_SQL}`);
} catch (err) {
  console.error('Error writing output file:', err.message);
}