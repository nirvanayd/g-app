const strUtil = require('../util/stringUtil')
const puppeteer = require('puppeteer')

class AIMONS {
    constructor() {
    }

    async ready(page) {
        const iPhone = puppeteer.devices['iPhone 12']
        await page.emulate(iPhone);
    }

    async getPrice(page) {
        // var price = document.querySelector('.info-box .price-info .price').innerText;
        try {
            const price = await page.evaluate(() => {
                return document.querySelector('.info-box .price-info .price').innerText;
            })
            return strUtil.replacePrice(price);
        } catch ( Error ) {
            console.log('price error');
            return null;
        }
    }

    async getName(page) {
        // document.querySelector('.info-box .name').innerText;
        try {
            return await page.evaluate(() => {
                return document.querySelector('.info-box .name').innerText;
            })
        } catch ( Error ) {
            console.log('name error');
            return null;
        }
    }

    async getImageList(page) {
        // var productImage = document.querySelectorAll('.head-info-wrap article.head-slide .swiper-slide img').forEach((image) => console.log(image.src))
        try {
            return await page.evaluate(() => {
                return Array.from(
                    document.querySelectorAll('.head-info-wrap article.head-slide .swiper-slide img')
                ).map((image) => image.src);
            })
        } catch ( Error ) {
            console.log('image error');
            return null;
        }
    }
}

module.exports = new AIMONS();