import express, { Application, Request, Response, NextFunction } from 'express'
import cookieParser from 'cookie-parser'
import bodyParser from 'body-parser';

import { parseJsonFile, generateHash} from './misc'
import authenticate from './middleware/authenticate';
import userAuthRouter from './route/userAuth'
import infoRouter from './route/info'
import imgHostRouter from './route/imgHost'
import recommendRouter from './route/recommendation'
import reviewRouter from './route/review'
import shippingRouter from './route/shipping'
import path from 'path';

const configFile = "../config.json"
const config: any = parseJsonFile(configFile)

const app: Application = express();
app.use(express.static(path.join(__dirname, '../public')))
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser())


app.use(authenticate)
app.use(userAuthRouter).use(infoRouter).use(imgHostRouter).use(recommendRouter).use(reviewRouter).use(shippingRouter)

app.get('/', (req: Request, res: Response, next: NextFunction) => {
    res.send('Hello World!');
});


app.listen(config.port, function () {
    console.log(`app listening on port ${config.port}`);
})

