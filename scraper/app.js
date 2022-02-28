require('dotenv').config();
const express = require('express')
const database = require('./database')
const fs = require('fs')

const app = express()
const port = process.env.APP_PORT;

// logger
const morgan = require('morgan');
const logger = require('./logger/winton');
const combined = ':remote-addr - :remote-user ":method :url HTTP/:http-version" :status :res[content-length] ":referrer" ":user-agent"'
const morganFormat = process.env.NODE_ENV !== 'prod"' ? 'dev' : combined; // NOTE: morgan 출력 형태 server.env에서 NODE_ENV 설정 production : 배포 dev : 개발
const bodyParser = require('body-parser')
app.use(morgan(morganFormat, {stream : logger.stream}));
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

app.get('/', (req, res) => {
    res.send('Hello World!')
})

app.post('/', (req, res) => {
    console.log('post sample');
    console.log(req.body);
    res.send('Hello World!')
})

app.post('/add-item', async (req, res) => {
    console.log('add item start..');
    const moduleName = req.body.moduleName;
    const url = req.body.url;

    let message = null;

    console.log('request info ', moduleName, url);

    if (!moduleName) {
        // error
    }

    try {
        if (!fs.existsSync('./brands/' + moduleName + '.js')) {
            res.statusCode = 510;
            message = '브랜드 모듈 조회 실패';
            res.send(message);
            return;
        }
    } catch (err) {
        res.statusCode = 510;
        message = '브랜드 모듈 조회 실패';
        res.send(message);
        return;
    }

    // load puppeteer browser
    const Scraper = require('./puppeteer/scraper')
    const browser = await Scraper.getBrowser();

    const page = await browser.newPage();
    const BrandModule = require('./brands/' + moduleName)
    await BrandModule.ready(page);
    await page.goto(url);
    const price = await BrandModule.getPrice(page);
    const name = await BrandModule.getName(page);
    const imageList = await BrandModule.getImageList(page);

    // await page.close({});

    const obj = {
        'price': price,
        'name': name,
        'imageList': imageList
    }

    console.log(obj);
    res.send(JSON.stringify(obj));
});

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})