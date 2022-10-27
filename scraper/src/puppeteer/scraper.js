const puppeteer = require('puppeteer')

class Scraper {
    constructor() {
    }

    async getBrowser() {
        return await this.boot();
    }

    async boot(options={}) {
        let browser = null;

        const {
            goToTargetApp = true,
            headless = true,
            devtools = false,
            slowMo = false
        } = options;

        browser = await puppeteer.launch({
            headless,
            devtools,
            ...(slowMo && { slowMo })
        });
        return browser;
    }
}

module.exports = new Scraper();


<html>
<div id='image'>

</div>
</html>