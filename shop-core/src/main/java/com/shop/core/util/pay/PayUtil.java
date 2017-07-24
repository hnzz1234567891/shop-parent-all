package com.shop.core.util.pay;

import com.shop.core.constant.PayConstant;

/**
 * Created by TW on 2017/6/19.
 */
public class PayUtil {

    /**
     * 生成一个sign签名
     * @param orderNo
     * @param totalFee
     * @return
     */
    public static String buildSign(String orderNo, String totalFee) {
        StringBuffer preStr = new StringBuffer();
        preStr.append("body=").append(PayConstant.DESC).append("&notify_url=").append(PayConstant.NOTIFY_URL)
                .append("&out_order_no=").append(orderNo).append("&partner=").append(PayConstant.PARTNER)
                .append("&return_url=").append(PayConstant.RETURN_URL).append("&subject=").append(PayConstant.SUBJECT)
                .append("&total_fee=").append(totalFee).append("&user_seller=").append(PayConstant.USER_SELLER);

        Md5Util md5Util = new Md5Util();
        String sign = md5Util.encode(preStr + PayConstant.KEY, null);
        return  sign;
    }

}
