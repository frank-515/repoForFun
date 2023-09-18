import express, { Request, Response, response } from 'express';
import { OkPacket, QueryError, RowDataPacket } from 'mysql2';
import pool from '../database'

const insertShippingOrderSQL = "INSERT INTO order_info (user_id, product_id, payment_method, address, contact_number, shipping_method)VALUES (?, ?, ?, ?, ?, ?);"
const getShippingInfoByID_SQL = "SELECT oi.*, ui.*, pi.* FROM order_info oi JOIN user_info ui ON oi.user_id = ui.user_id JOIN product_info pi ON oi.product_id = pi.product_id WHERE oi.order_id = ?"
const getOrderInfoByUserID_SQL = "SELECT oi.*, pi.product_name, pi.product_price FROM order_info oi JOIN product_info pi ON oi.product_id = pi.product_id WHERE oi.user_id = ?"
const UpdateQuantity_SQL = "UPDATE product_info SET product_quantity = product_quantity - ? WHERE product_id = ?"

const router = express.Router();

// @param {user_id} {product_id} {payment_method?}
// @param {address} {contact_number} {shipping_method}
router.post('/api/buy', (req: Request, res: Response) => {
    let {user_id, product_id, payment_method, address, contact_number, shipping_method} = req.body
    if (!payment_method) payment_method = ''
    if (!shipping_method) shipping_method = 'Express'
    pool.execute(insertShippingOrderSQL, [user_id, product_id, payment_method , address, contact_number, shipping_method], (err, result: OkPacket) => {
        if (err) {
            res.status(503)
            console.error(err.message);
        }
        res.status(200).json({id: result.insertId})
    })
})
// @param {id}
router.post('/api/shipment', (req: Request, res: Response) => {
    const { id } = req.body;
    pool.execute(getShippingInfoByID_SQL, [id], (err, result: any) => {
        if (err) {
            res.sendStatus(503)
            console.error(err.message);
            
        } else if (result.length) {
            res.status(200).json(result[0])
        } else {
            res.sendStatus(503)
        }
    })
})

router.post('/api/user-order', (req: Request, res: Response) => {
    const {user_id } = req.body
    pool.query(getOrderInfoByUserID_SQL, [user_id], (err, result) => {
        if (err) {
            console.error(err.message);
            res.sendStatus(503);
        }
        else res.status(200).json(result);
    })
})
//@param {product_id}
router.post('/api/buy-c', (req: Request, res: Response) => {
    const {product_id , count} = req.body
    pool.query(UpdateQuantity_SQL, [count, product_id], (err, result) => {
        if (err) {
            console.error(err.message);
            res.sendStatus(503);
        }
        else res.status(200).json(result);
    })
})


export default router

