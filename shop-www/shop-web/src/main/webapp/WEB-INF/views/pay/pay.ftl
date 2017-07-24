<!DOCTYPE html >
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>支付跳转中...</title>
</head>
<body style="background:#F3F3F4">
    <form id='paysubmit' name='paysubmit' action='http://www.passpay.net/PayOrder/payorder' accept-charset='utf-8' method='post'>

        <input type='hidden' name='sign' value='${payRequestVo.sign}'/>

        <input type='hidden' name='body' value='${payRequestVo.body}'/>

        <input type='hidden' name='user_seller' value='${payRequestVo.userSeller}'/>

        <input type='hidden' name='total_fee' value='${payRequestVo.totalFee}'/>

        <input type='hidden' name='subject' value='${payRequestVo.subject}'/>

        <input type='hidden' name='notify_url' value='${payRequestVo.notifyUrl}'/>

        <input type='hidden' name='out_order_no' value='${payRequestVo.outOrderNo}'/>

        <input type='hidden' name='partner' value='${payRequestVo.partner}'/>

        <input type='hidden' name='return_url' value='${payRequestVo.returnUrl}'/>

        <input type='submit'  value='支付进行中...'  style='display:none;'/>
    </form>
<script type="text/javascript">
    document.forms['paysubmit'].submit();
//    function tijiao() {
//        document.forms['paysubmit'].submit();
//    }
</script>
</body>
</html>
