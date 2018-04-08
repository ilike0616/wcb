package com.uniproud.wcb

import static com.uniproud.wcb.ErrorUtil.getSuccessTrue

class DocController {

    def phone() {
        log.info params
        render(successTrue)
    }
}
