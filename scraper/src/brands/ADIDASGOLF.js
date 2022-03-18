const strUtil = require('../util/stringUtil')

class ADIDASGOLF {
    constructor() {
    }

    async ready(page) {
    }

    async getPrice(page) {
        // var price = document.querySelector('#span_product_price_text').innerText;
        try {
            return null;
        } catch ( Error ) {
            console.log('price error');
            return null;
        }
    }

    async getName(page) {
        try {
            return null;
        } catch ( Error ) {
            console.log('name error');
            return null;
        }
    }

    async getImageList(page) {
        try {
            return null;
        } catch ( Error ) {
            console.log('image error');
            return null;
        }
    }
}

module.exports = new ADIDASGOLF();