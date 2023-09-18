import * as crypto from 'crypto';
import * as fs from 'fs';
import * as path from 'path';

const config: any = parseJsonFile('../config.json')

function parseJsonFile(filePath: string): object | null {
    try {
        const fileContent = fs.readFileSync(path.join(__dirname, filePath), 'utf-8');
        const parsedObject = JSON.parse(fileContent);
        return parsedObject;
    } catch (error: any) {
        console.error('解析JSON文件时出错:', error.message);
        return null;
    }
}

function generateHash(password: string): string {
    const salt = config.salt; // 盐值
    const passwordWithSalt = password + salt;
    const hash = crypto.createHash('sha256').update(passwordWithSalt).digest();
    const hashBase64 = hash.toString('base64');
    return hashBase64;
}

export {
    parseJsonFile, generateHash
}
