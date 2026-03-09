const fs = require("fs");
const path = require("path");

const folder = __dirname;
const outputFile = path.join(folder, "books.sql");

function escapeSQL(text) {
    return text
        .replace(/'/g, "''"); // escape single quotes for SQL
}

fs.readdir(folder, (err, files) => {
    if (err) {
        console.error(err);
        return;
    }

    const txtFiles = files.filter(file => path.extname(file) === ".txt");

    let sqlOutput = "";

    txtFiles.forEach(file => {
        const filePath = path.join(folder, file);
        const content = fs.readFileSync(filePath, "utf8").trim();

        const wordCount = content.split(/\s+/).length;
        const preview = content.substring(0, 200);
        const title = path.basename(file, ".txt");

        const escapedFull = escapeSQL(content);
        const escapedPreview = escapeSQL(preview);
        const escapedTitle = escapeSQL(title);

        const sql = `INSERT OR IGNORE INTO books (title, word_count, preview, full_text) VALUES ('${escapedTitle}', ${wordCount}, '${escapedPreview}', '${escapedFull}');\n\n`;

        sqlOutput += sql;
    });

    fs.writeFileSync(outputFile, sqlOutput, "utf8");

    console.log(`Generated ${outputFile} with ${txtFiles.length} inserts.`);
});