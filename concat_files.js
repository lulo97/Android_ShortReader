const fs = require("fs");
const path = require("path");

const ROOT_DIR = process.cwd();
const OUTPUT_FILE = "all_code.txt";

function shouldInclude(file) {
    if (file.endsWith("common-english-viet-words.txt") ||
        file.endsWith("04_seed_word_details.sql") ||
        file.endsWith("vocabularies.json")) {
        return false
    }
    return (
        file.endsWith(".kt") ||
        file.endsWith(".sql") ||

        file.toLowerCase() === "androidmanifest.xml"
    );
}

function walk(dir, fileList = []) {
    const files = fs.readdirSync(dir);

    files.forEach(file => {
        const fullPath = path.join(dir, file);
        const stat = fs.statSync(fullPath);

        if (stat.isDirectory()) {

            // Skip build folders
            if (
                file === "build" ||
                file === ".gradle" ||
                file === ".idea" ||
                file === "node_modules"
            ) {
                return;
            }

            walk(fullPath, fileList);

        } else {
            if (shouldInclude(file)) {
                fileList.push(fullPath);
            }
        }
    });

    return fileList;
}

function concatFiles() {

    const files = walk(ROOT_DIR);

    let output = "";

    files.forEach(file => {

        const relative = path.relative(ROOT_DIR, file);
        const content = fs.readFileSync(file, "utf8");

        output += `=== START ${relative} ===\n\n`;
        output += content;
        output += `\n\n=== END ${relative} ===\n\n`;
    });

    fs.writeFileSync(OUTPUT_FILE, output);

    console.log(`Done. Output written to ${OUTPUT_FILE}`);
}

concatFiles();