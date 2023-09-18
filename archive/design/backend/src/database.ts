import mysql from 'mysql2'
import { parseJsonFile } from './misc'

const config:any = parseJsonFile('../config.json')

const pool = mysql.createPool(config.databaseConfig)

export default pool;