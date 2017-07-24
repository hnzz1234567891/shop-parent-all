package com.shop.service;

import com.shop.core.dto.MemberDto;
import com.shop.core.model.Member;
import com.shop.core.util.AssertUtil;
import com.shop.core.util.MD5;
import com.shop.core.vo.LoginIndentity;
import com.shop.dao.MemberDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by TW on 2017/6/14.
 */
@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    /**
     * 注册
     * @param memberDto
     */
    public LoginIndentity insert(MemberDto memberDto, String sessionVerifyCode,
                       String sessionPhoneVerifyCode) {
//        1、基本参数验证
//          a) 非空验证
//          b) 短信验证码
//          c) 图片验证码
//        2、用户名唯一
//        3、邮箱唯一
//        4、手机号唯一
//        5、插入数据库--member
//        6、构建登录用户信息
// 基本参数验证
        checkRegisterParams(memberDto, sessionVerifyCode, sessionPhoneVerifyCode);

        // 唯一性验证：username email phone
        checkResgisterUnique(memberDto);

        // 保存到数据库
        Member member = new Member();
        String password = memberDto.getPassword();
        BeanUtils.copyProperties(memberDto, member);
        member.setPassword(MD5.toMD5(password)); // 密码加密
        memberDao.insert(member);

        // 构建一个登录信息结果集
        LoginIndentity loginIndentity = buildLoginIndentity(member);

        return loginIndentity;
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @param verifyCode
     * @param sessionVerifyCode
     * @return
     */
    public LoginIndentity login(String userName, String password,
                                String verifyCode, String sessionVerifyCode) {

        // 基本基本参数验证
        // 根据用户名查询用户信息
        // 判断用户是否存在
        // MD5(password) 比较密码是否一致
        // 构建返回实体
        AssertUtil.notNull(userName, "请输入用户名或邮箱");
        AssertUtil.notNull(password, "请输入密码");
        AssertUtil.notNull(verifyCode, "请输入验证码");
        AssertUtil.isTrue(!verifyCode.equals(sessionVerifyCode), "验证码输入有误，请重新输入");
        // 根据userName(username, email) 去数据库查询 --验证
        Member member = memberDao.findByUserNameOrEmail(userName);
        AssertUtil.isTrue(member == null, "用户名或者密码错误，请重新输入");

        // 验证password --加密验证
        password = MD5.toMD5(password);
        AssertUtil.isTrue(!password.equals(member.getPassword()), "用户名或者密码错误，请重新输入");

        // 构建一个登录信息
        LoginIndentity loginIndentity = buildLoginIndentity(member);
        return loginIndentity;

    }


    /**
     * 注册时基本参数验证
     * @param memberDto
     * @param sessionVerifyCode
     * @param sessionPhoneVerifyCode
     */
    private static void checkRegisterParams(MemberDto memberDto,
                                            String sessionVerifyCode, String sessionPhoneVerifyCode) {
        AssertUtil.notNull(memberDto.getUsername(), "请输入用户名");
        AssertUtil.notNull(memberDto.getPassword(), "请输入密码");
        AssertUtil.isTrue(!memberDto.getPassword().equals(memberDto.getRePassword()), "两次密码输入不相同");
        AssertUtil.notNull(memberDto.getEmail(), "请输入邮箱");
        AssertUtil.notNull(memberDto.getPhone(), "请输入手机号");
        AssertUtil.notNull(memberDto.getPhoneVerifyCode(), "请输入手机验证码");
        AssertUtil.isTrue(!memberDto.getPhoneVerifyCode().toLowerCase().equals(sessionPhoneVerifyCode), "手机验证码输入有误，请重新输入");
        AssertUtil.notNull(memberDto.getVerifyCode(), "请输入验证码");
        AssertUtil.isTrue(!memberDto.getVerifyCode().toLowerCase().equals(sessionVerifyCode), "验证码输入有误，请重新输入");
    }

    /**
     * 唯一性验证
     * @param memberDto
     */
    private void checkResgisterUnique (MemberDto memberDto) {
        Member member = memberDao.findByColumn("username", memberDto.getUsername());
        AssertUtil.isTrue(member != null, "该用户名已注册");
        member = memberDao.findByColumn("email", memberDto.getEmail());
        AssertUtil.isTrue(member != null, "该邮箱已注册");
        member = memberDao.findByColumn("phone", memberDto.getPhone());
        AssertUtil.isTrue(member != null, "该手机号已注册");
    }


    /**
     * 构建LoginIdentity结果集
     * @param member
     * @return
     */
    private LoginIndentity buildLoginIndentity(Member member) {
        LoginIndentity loginIndentity = new LoginIndentity();
        BeanUtils.copyProperties(member, loginIndentity);
        return loginIndentity;
    }
}
