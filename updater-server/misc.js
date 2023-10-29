const fs = require('fs');
const json5 = require('json5');
const crypto = require('crypto');
const path = require('path')
const {log} = require("debug");


const getConfig = () => {
    let config = undefined;
    try {
        config = parseJsonFromFile(path.join(__dirname, './config.json'))
    } catch (error) {
        console.error(error.message)
    }
    return config
}
const config = getConfig()

function parseJsonFromFile(filePath) {
    try {
        const fileData = fs.readFileSync(filePath, 'utf8');
        return json5.parse(fileData);
    } catch (error) {
        console.error('Error parsing JSON from file:', error);
        return null;
    }
}

function generateSHAInfo(filePath) {
    const jsonFilePath = path.join(filePath, 'SHA256.json');

    if (fs.existsSync(jsonFilePath)) {
        fs.unlinkSync(jsonFilePath);
        console.log(`Deleted existing ${jsonFilePath} file.`);
    }

    let shaInfo = [];

    function getFiles(dir) {
        const files = fs.readdirSync(dir);

        for (const file of files) {
            const filePath = path.join(dir, file);
            const stat = fs.statSync(filePath);

            if (stat.isDirectory()) {
                getFiles(filePath);
            } else {
                const sha256 = crypto.createHash('sha256');
                const data = fs.readFileSync(filePath);

                sha256.update(data);
                shaInfo.push({
                    path: config.static_prefix + '/' + path.relative(config.hosting_directory, filePath).replace(/\\/g, '/'),
                    digest: sha256.digest('hex')
                });
            }
        }
    }

    getFiles(filePath);
    const json = JSON.stringify(shaInfo, null, 2);

    fs.writeFile(jsonFilePath, json, (err) => {
        if (err) {
            console.error(err);
            return;
        }
        console.log(`Generated digest for ${filePath} and saved it to ${jsonFilePath}.`);
    });
}

function digestForSubFolders(filePath) {
    const subFolders = fs.readdirSync(filePath, { withFileTypes: true })
        .filter(dirent => dirent.isDirectory())
        .map(dirent => path.join(filePath, dirent.name));

    for (const subFolder of subFolders) {
        generateSHAInfo(subFolder);
    }
}

module.exports = {
    parseJsonFromFile, generateSHAInfo, digestForSubFolders, getConfig
}
