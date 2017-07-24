package com.shop.core.constant;
/**
 * 
 * Description：常量 <br>
 * 
 * ClassName：Constant <br>
 * 
 * Date：2016年6月25日下午11:42:09 <br>
 * 
 * Version：v1.0 <br>
 * 
 */
public class PayConstant {
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/**
	 * 合作身份者PID，签约账号，由16位纯数字组成的字符串，请登录商户
	 */
	public final static String  PARTNER = "445862206082341";
	
	/**
	 *  MD5密钥，安全检验码，由数字和字母组成的32位字符串，请登录商户后
	 */
	public final static String KEY = "ffzJTAav4VINvFXuSNiUnyWh8a6tEwxf";
	
	/**
	 * 商户号（6位数字)
	 */
	public final static String  USER_SELLER = "929414";
	
	/**
	 *  服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可
	 */
	public final static String  NOTIFY_URL = "http://www.shsxt.com:8082/pay/notify_url";
	
	/**
	 * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 */
	public final static String  RETURN_URL = "http://www.shsxt.com:8082/pay/return_url";
	
	/**
	 * 支付地址，必须外网可以正常访问
	 */
	public final static String  GATEWAY_NEW = "http://www.passpay.net/PayOrder/payorder";


	public final static String SUBJECT = "订单支付";

	public final static String  CALLBACK_FAILURE = "支付失败，请联系客服。。。";

	public final static String  TRADE_SUCCESS = "TRADE_SUCCESS";

	public final static String DESC = "订单描述";
}
