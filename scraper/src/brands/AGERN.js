const strUtil = require('../util/stringUtil')
const puppeteer = require("puppeteer");

class AGERN {
    constructor() {
    }

    async ready(page) {
        const iPhone = puppeteer.devices['iPhone 12']
        await page.emulate(iPhone);
    }

    async getPrice(page) {
        // var price = document.querySelector('#span_product_price_text').innerText;
        try {
            const price = await page.evaluate(() => {
                return document.querySelector('#span_product_price_text').innerText;
            })
            return strUtil.replacePrice(price);
        } catch ( Error ) {
            console.log('price error');
            return null;
        }
    }

    async getName(page) {
        // var name = document.querySelector('.prdDesc .name').innerText;
        try {
            return await page.evaluate(() => {
                return document.querySelector('.prdDesc .name').innerText;
            })
        } catch ( Error ) {
            console.log('name error');
            return null;
        }
    }

    async getImageList(page) {
        // var productImage = document.querySelectorAll('.prdImgView p.thumbnail img').forEach((image) => console.log(image.src));
        try {
            return await page.evaluate(() => {
                return Array.from(
                    document.querySelectorAll('.prdImgView p.thumbnail img')
                ).map((image) => image.src);
            })
        } catch ( Error ) {
            console.log('image error');
            return null;
        }
    }
}

module.exports = new AGERN();