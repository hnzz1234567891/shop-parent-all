package com.shop.service;

import com.shop.core.model.Area;
import com.shop.core.model.Receiver;
import com.shop.core.util.AssertUtil;
import com.shop.dao.AreaDao;
import com.shop.dao.ReceiverDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by TW on 2017/6/18.
 */
@Service
public class ReceiverService {

    @Autowired
    private ReceiverDao receiverDao;
    @Autowired
    private AreaDao areaDao;

    /**
     * 查询用户收货地址
     * @param loginUserId
     * @return
     */
    public List<Receiver> findUserReceiver(Integer loginUserId) {

        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");

        List<Receiver> receivers = receiverDao.findUserReceiver(loginUserId);

        return receivers;

    }

    /**
     * 添加用户收货地址
     * @param consignee
     * @param areaId
     * @param address
     * @param zipCode
     * @param phone
     * @param isDefault
     * @param loginUserId
     */
    public Receiver add(String consignee, Integer areaId, String address, String zipCode,
                        String phone, Boolean isDefault, Integer loginUserId) {

        // 基本参数验证
        AssertUtil.isTrue(StringUtils.isBlank(consignee), "请输入收货人");
        AssertUtil.isTrue(areaId == null || areaId < 1, "请选择收货地区");
        AssertUtil.isTrue(StringUtils.isBlank(address), "请输入收货地址");
        AssertUtil.isTrue(StringUtils.isBlank(zipCode), "请输入邮政编码");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "请输入收货人手机号");
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
        // 判断是否是默认
        if (isDefault) {
            // 把其他默认取消
            receiverDao.updateDefault(loginUserId);
        }

        // 查看用户收货地址的个数
        Integer count = receiverDao.count(loginUserId);
        AssertUtil.isTrue(count != null && count >= Receiver.MAX_RECEIVER_COUNT,
                "您最多只能添加" + Receiver.MAX_RECEIVER_COUNT + "个收货地址");

        // 查询此地区是否添加过
        Receiver receiverFromDb = receiverDao.findMemeberAreaReceiver(loginUserId, areaId,
                consignee, address);
        AssertUtil.isTrue(receiverFromDb != null, "这个地址已经存在，请重现选择");
        // 插入数据库
        Area area = areaDao.findById(areaId);
        AssertUtil.isTrue(area == null, "该地址不存在，请重新选择");

        Receiver receiver = new Receiver();
        receiver.setAddress(address);
        receiver.setArea(areaId);
        receiver.setAreaName(area.getFullName());
        receiver.setConsignee(consignee);
        receiver.setIsDefault(isDefault);
        receiver.setMember(loginUserId);
        receiver.setPhone(phone);
        receiver.setZipCode(zipCode);
        receiver.setCreateDate(new Date());
        receiver.setModifyDate(new Date());
        receiver.setVersion(0);
        receiverDao.insert(receiver);
        return receiver;
    }

    /**
     * 根据ID获取收货地址
     * @param receiverId
     * @return
     */
    public Receiver findById(Integer receiverId) {
        AssertUtil.isTrue(receiverId == null || receiverId < 1, "请选择收货地址");
        Receiver receiver = receiverDao.findById(receiverId);
        return receiver;
    }
}
