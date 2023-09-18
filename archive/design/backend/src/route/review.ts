import express, { Request, Response, response } from 'express';
import { QueryError, RowDataPacket } from 'mysql2';
import pool from '../database'

const getProductReviewByProductID_SQL = "SELECT * FROM review WHERE product_id = ?;"
const getProductReviewByUserID_SQL = "SELECT * FROM review WHERE user_id = ?;"
const getProductReviewByID_SQL = "SELECT r.*, u.username, u.user_avatar FROM review r JOIN user_info u ON r.user_id = u.user_id WHERE r.review_id = ?;"
const insertNewReviewSQL = "INSERT INTO review (user_id, star, content, product_id) VALUES (?, ?, ?, ?);"


const router = express.Router();

// @param {user_id} {star: number} {content} {product_id}
router.post('/api/write-review', async (req: Request, res: Response) => {
    const { user_id, product_id, content, star } = req.body
    
    pool.execute(insertNewReviewSQL, [user_id, star, content, Number.parseInt(product_id)], (err, _) => {
        if (err) {
            console.error(err.message);
            res.status(503).end(err.message)
        } else {
            res.sendStatus(200)
        }
    })
    
})

// @param {product_id} / {user_id} / {review_id}
router.post('/api/review', async (req: Request, res: Response) => {
    const promisePool = pool.promise();
    let result: any
    const { product_id, user_id, review_id } = req.body;
    if (product_id) {
        result = await promisePool.query(getProductReviewByProductID_SQL, [product_id]);
    } else if (user_id) {
        result = await promisePool.query(getProductReviewByUserID_SQL, [user_id]);
    } else if (review_id) {
        result = await promisePool.query(getProductReviewByID_SQL, [review_id]);
    }
    
    if (result && result[0].length > 0) {
        res.send(result[0])
    } else {
        res.send([])
    }
})

export default router
