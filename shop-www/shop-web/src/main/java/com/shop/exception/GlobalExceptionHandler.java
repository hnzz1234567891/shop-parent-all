package com.shop.exception;

/**
 * Created by TW on 2017/6/17.
 */


import com.alibaba.fastjson.JSON;
import com.shop.core.base.BaseController;
import com.shop.core.base.ResultInfo;
import com.shop.core.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
//@RestControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ParamException.class)
//    @ResponseBody
    public String hander(ParamException paramException, Model model,
                         HttpServletRequest request, HttpServletResponse response) {
        logger.info("异常处理：{}", paramException);
        ResultInfo resultInfo = failure(paramException.getResultCode(), paramException.getMessage());
        String xmlHttpRequest = request.getHeader("X-Requested-With");
        // 如果是ajax请求返回json,否则跳转到错误页面
        if (StringUtils.isNotBlank(xmlHttpRequest) && "XMLHttpRequest".equals(xmlHttpRequest)) { //该请求是一个ajax请求
            response.setContentType("application/json;charset=UTF-8"); // MIME
            PrintWriter pw = null;
            try {
                pw = response.getWriter();
                pw.write(JSON.toJSONString(resultInfo));
            } catch (IOException e) {
                logger.error("返回JSON失败：{}", e);
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        } else {
            model.addAttribute("result", resultInfo);
            return "error";
        }
        return null;
    }



    @ExceptionHandler(value = {IllegalArgumentException.class, BindException.class})
    @ResponseBody
    public ResultInfo handerMutiException(Exception ex) {
        if (ex instanceof IllegalArgumentException) {

        } else if (ex instanceof  BindException) {

        }
        logger.error("异常信息：{}", ex);
        return failure(0, ex.getMessage());
    }

}
