const strUtil = require('../util/stringUtil')
const puppeteer = require('puppeteer')

class AIOL {
    constructor() {
    }

    async ready(page) {
        const iPhone = puppeteer.devices['iPhone 12']
        await page.emulate(iPhone);
    }

    async getPrice(page) {
        // var price = document.querySelector('#prod_goods_form .real_price').innerText;
        try {
            const price = await page.evaluate(() => {
                return document.querySelector('#prod_goods_form .view_tit').innerText;
            })
            return strUtil.replacePrice(price);
        } catch ( Error ) {
            console.log('price error');
            return null;
        }
    }

    async getName(page) {
        // document.querySelector('#prod_goods_form .view_tit').innerText;
        try {
            return await page.evaluate(() => {
                return document.querySelector('#prod_goods_form .view_tit').innerText;
            })
        } catch ( Error ) {
            console.log('name error');
            return null;
        }
    }

    async getImageList(page) {
        // var productImage = document.querySelectorAll('.goods_thumbs .owl-item img').forEach((image) => console.log(image.src));
        try {
            return await page.evaluate(() => {
                return Array.from(
                    document.querySelectorAll('.goods_thumbs .owl-item img')
                ).map((image) => image.src);
            })
        } catch ( Error ) {
            console.log('image error');
            return null;
        }
    }
}

module.exports = new AIOL();