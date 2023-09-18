import express, { Request, Response, NextFunction } from 'express';
import { QueryError, RowDataPacket } from 'mysql2';
import pool from '../database'
import { generateHash } from '../misc'


const router = express.Router();

const registerSQL = `INSERT INTO user_info (username, contact_number, default_address, password, gender, is_admin, email) VALUES (?, ?, ?, ?, ?, ?, ?);`
const loginSQL = "SELECT password, user_id FROM user_info WHERE username = ? LIMIT 1;"
const updateUserExpirationSQL = "UPDATE user_info SET cookie_token = ? WHERE username = ?;"

// @param {username} {password}
router.post('/api/login', (req: Request, res: Response) => {
    const { username, password } = req.body
    console.log(req.body);

    pool.execute(loginSQL, [username], (err, result: RowDataPacket[]) => {
        if (err) {
            console.error(err.message);
            res.status(503)
        } else if (result.length === 0) {
            res.status(200).json({ result: "failed" })
        } else if (result[0].password === generateHash(password)) {
            const token = Math.floor(Math.random() * 100000).toString()
            pool.execute(updateUserExpirationSQL, [token, username], (err, _: RowDataPacket[]) => {
                if (err) {
                    console.error(err.message);
                    res.status(503)
                } else {
                    res.cookie('token', token)
                    res.cookie('user_id', result[0].user_id)
                    res.cookie('username', username)
                    res.status(200).json({ result: "success", username: username })
                }
            })
        } else {
            res.status(200).json({ result: "failed" })
        }


    })
})

// @param {username} {contact_number} {default_address}
// @param {password} {gender} {is_admin: boolean} {email}
router.post('/api/register', (req: Request, res: Response) => {

    const { username, contact_number, default_address, password, gender, is_admin, email } = req.body

    pool.execute(registerSQL, [username, contact_number, default_address, generateHash(password), gender, is_admin ? true : false, email], (err, result) => {
        if (err) {
            console.error(err.message);
            res.status(200).json({ status: "failed" })
        } else {
            res.status(200).json({ status: "success" })
        }

    })
})

export default router
