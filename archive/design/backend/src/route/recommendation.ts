import express, { Request, Response, response } from 'express';
import { QueryError, RowDataPacket } from 'mysql2';
import pool from '../database'


const getRandomProductSQL = "SELECT * FROM product_info ORDER BY RAND() LIMIT ?;"

const router = express.Router();

// @param {item: number} 
router.post('/api/recommend', async (req: Request, res: Response) => {

    const { item, simple } = req.body
    const promisePool = pool.promise()
    const result: any = await promisePool.query(getRandomProductSQL, [item])
    res.send(result[0])
})

export default router