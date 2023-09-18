import express, { NextFunction, Request, Response } from "express";

const router = express.Router();

const authenticate = (req: Request, res: Response, next: NextFunction) => {
    // 中间件逻辑代码
    console.log('This is my middleware');
    
    
    // 继续向下执行请求处理程序
    next();
};

router.get('/a/*', authenticate);

export default router;