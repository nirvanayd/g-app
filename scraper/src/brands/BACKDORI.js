const puppeteer = require('puppeteer')
const strUtil = require('../util/stringUtil')

class BACKDORI {
    constructor() {
    }

    async ready(page) {
        const iPhone = puppeteer.devices['iPhone 12']
        await page.emulate(iPhone);
    }

    async getPrice(page) {
        // document.querySelector('#span_product_price_text').innerText;
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
        try {
            return await page.evaluate(() => {
                return Array.from(
                    document.querySelectorAll('.prdImgView p.prdImg img')
                ).map((image) => image.src);
            })
        } catch ( Error ) {
            console.log('image error');
            return null;
        }
    }
}

module.exports = new BACKDORI();