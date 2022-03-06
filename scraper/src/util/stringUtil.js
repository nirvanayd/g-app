
function replacePrice(priceStr) {
    return priceStr.replace(/,/gi, '').replace(/원/, '')
}

exports.replacePrice = replacePrice;