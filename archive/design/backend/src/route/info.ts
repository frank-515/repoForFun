import express, { Request, Response, response } from 'express';
import { QueryError, RowDataPacket } from 'mysql2';
import pool from '../database'




const getUserInfoByID_SQL = "SELECT * FROM user_info WHERE user_id = ?;"
const getUserInfoByNameSQL = "SELECT * FROM user_info WHERE username = ?;"
const getProductInfoSQL = "SELECT * FROM product_info WHERE product_id = ?;"
const getUserBuyInfoSQL = "SELECT oi.*, pi.* FROM order_info oi JOIN product_info pi ON oi.product_id = pi.product_id WHERE oi.user_id = ? ORDER BY oi.order_date DESC"
const updateUserInfoSQL = "UPDATE user_info SET username = ?, gender = ?, email = ?, contact_number = ?, default_address = ?, user_avatar = ? WHERE user_id = ?"
const getProductInfoByNameSplitPageSQL = 'SELECT DISTINCT * FROM product_info WHERE product_name LIKE CONCAT("%", ?, "%") OR product_description LIKE CONCAT("%", ?, "%") ORDER BY product_id LIMIT ?, ?'
const getProductIDByNameSplitPageSQL = "SELECT DISTINCT product_id FROM product_info WHERE product_name LIKE CONCAT('%', ?, '%') OR product_description LIKE CONCAT('%', ?, '%') ORDER BY product_id LIMIT ?, ?"
const deleteProductSQL = "DELETE FROM product_info WHERE product_id = ?"
const updateProductSQL = "UPDATE product_info SET product_name = ?, product_quantity = ?, product_price = ?, product_type = ?, product_description = ?, is_recommended = ?, point_ratio = ?, product_image = ? WHERE product_id = ?;"
const insertProductSQL = "INSERT INTO product_info (product_name, product_quantity, product_price, product_type, product_description, is_recommended, point_ratio, product_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?) "


const router = express.Router();


// @param {username} / {user_id} 
router.post('/api/user', async (req: Request, res: Response) => {

    const { username, user_id } = req.body;

    const promisePool = pool.promise();
    let result: any
    if (username) {
        result = await promisePool.query(getUserInfoByNameSQL, [username]);
    } else if (user_id) {
        result = await promisePool.query(getUserInfoByID_SQL, [user_id]);
    }

    if (result && result[0].length > 0) {
        res.send(result[0][0])
    } else {
        res.send({ username: '' })
    }
})

// @param {product_id} 
router.post('/api/product', async (req: Request, res: Response) => {

    const { product_id } = req.body;
    const promisePool = pool.promise();
    const result: any = await promisePool.query(getProductInfoSQL, [product_id]);
    if (result && result[0].length > 0) {
        res.send(result[0][0])
    } else {
        res.send({ product_id: '' })
    }
})

// @param { user_id }
router.post('/api/record/consume', (req: Request, res: Response) => {
    const { user_id } = req.body
    pool.query(getUserBuyInfoSQL, [user_id], (err, result: any) => {
        if (err) console.error(err.message);

        res.send(result);

    })

})


// UPDATE user_info SET username = ?, gender = ?, email = ?, contact_number = ?, default_address = ? WHERE user_id = ?
// @param { username } { gender } { email } { contact_number } { default_address } // { user_id }
router.post('/api/update-user-info', (req: Request, res: Response) => {
    const { username, gender, contact_number, default_address, email, user_id, user_avatar } = req.body;
    pool.execute(updateUserInfoSQL, [username, gender, email, contact_number, default_address, user_avatar ,user_id], (err, result) => {
        if (err) {
            console.error(err);
            res.sendStatus(503)
        }
        res.sendStatus(200)

    })

})

// @param {keyword} {page} {page_size?} {id_only?}

router.post('/api/search/product', (req: Request, res: Response) => {
    const defaultPageSize = 20;
    let { keyword, page, page_size, id_only } = req.body;
    const offset = (page - 1) * (page_size || defaultPageSize);
    pool.query(id_only ? getProductIDByNameSplitPageSQL : getProductInfoByNameSplitPageSQL, [keyword, keyword, offset, page_size || defaultPageSize], (error, result) => {
        if (error) {
            console.error(error.message);
            res.sendStatus(503)
        }
        res.send(result)
    })
})
// @param product_name, product_quantity, product_price, product_type, product_description, is_recommended, point_ratio, product_image product_id
router.post('/api/insert/product', (req: Request, res: Response) => {
    const {product_name, product_quantity, product_price, product_type, product_description, is_recommended, point_ratio, product_image, product_id} = req.body
    pool.execute(updateProductSQL, [product_name, product_quantity, product_price, product_type, product_description, is_recommended, point_ratio, product_image, product_id], (err, result) => {
        if (err) {
            res.sendStatus(503)
            console.error(err.message);
        }
        res.sendStatus(200)
    })
})

// @param product_name, product_quantity, product_price, product_type, product_description, is_recommended, point_ratio, product_image product_id
router.post('/api/insert-product', (req: Request, res: Response) => {
    const {product_name, product_quantity, product_price, product_type, product_description, is_recommended, point_ratio, product_image} = req.body
    pool.execute(insertProductSQL, [product_name, product_quantity, product_price, product_type, product_description, is_recommended, point_ratio, product_image], (err, result) => {
        if (err) {
            res.sendStatus(503)
            console.error(err.message);
        }
        res.sendStatus(200)
    })
})

router.post('/api/delete/product', (req: Request, res: Response) => {
    const {product_id} = req.body;
    pool.execute(deleteProductSQL, [product_id], (err, _) => {
        if (err) {
            res.sendStatus(503)
            console.error(err.message);
        }
        res.sendStatus(200)
    })
})


export default router
