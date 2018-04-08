package com.uniproud.wcb

class QRCodeController {
    def qRCodeService
    def getQRCode(MarketActivity ma) {
        println 'getQRCode'
        qRCodeService.getTicket()
    }
}
