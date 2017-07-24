package com.shop.dao;

import com.shop.core.model.Receiver;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by TW on 2017/6/18.
 */
public interface ReceiverDao {

    @Select("select id, address, consignee, phone, zip_code, area, area_name, "
            + "member, is_default from xx_receiver where member = #{memberId} and version = 0")
    List<Receiver> findUserReceiver(@Param(value = "memberId") Integer loginUserId);

    @Update("update xx_receiver set is_default = 0 where member = #{memberId} and version = 0")
    void updateDefault(@Param(value = "memberId")Integer loginUserId);

    @Select("select count(id) from xx_receiver where member = #{memberId} and version = 0")
    Integer count(@Param(value = "memberId")Integer loginUserId);

    @Select("select id, address, consignee, phone, zip_code, area, area_name, "
            + "member, is_default from xx_receiver where member = #{memberId} and area = #{areaId} " +
            " and consignee = #{consignee} and address = #{address} and version = 0")
    Receiver findMemeberAreaReceiver(@Param(value = "memberId")Integer loginUserId,
                                     @Param(value = "areaId")Integer areaId, @Param(value = "consignee")String consignee,
                                     @Param(value = "address")String address);

    void insert(Receiver receiver);

    @Select("select id, address, consignee, phone, zip_code, area, area_name, "
            + "member, is_default from xx_receiver where id = #{id} and version = 0")
    Receiver findById(@Param(value="id")Integer receiverId);
}
