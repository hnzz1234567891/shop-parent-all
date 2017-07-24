package com.shop.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.shop.core.constant.Gender;

/**
 * 自定义Gender转换类
 * 存储属性：Gender.getType()
 * JDBCType：int
 */
public class GenderHandler extends BaseTypeHandler<Gender> {

	private Class<Gender> Gender;

	private final Gender[] enums;

	/**
	 * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
	 * @param type 配置文件中设置的转换类
	 */
	public GenderHandler(Class<Gender> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.Gender = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null) {
			throw new IllegalArgumentException(type.getSimpleName()
					+ " does not represent an enum type.");
		}
	}

	@Override
	public Gender getNullableResult(ResultSet rs, String columnName) throws SQLException {
		// 根据数据库存储类型决定获取类型，数据库中存放int类型
		int i = rs.getInt(columnName);
		if (rs.wasNull()) {
			return null;
		}
		// 根据数据库中的code值，定位Gender子类
		return locateGender(i);
	}

	@Override
	public Gender getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型，数据库中存放int类型
		int i = rs.getInt(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		// 根据数据库中的code值，定位Gender子类
		return locateGender(i);
	}

	@Override
	public Gender getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// 根据数据库存储类型决定获取类型，数据库中存放int类型
		int i = cs.getInt(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		// 根据数据库中的code值，定位Gender子类
		return locateGender(i);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Gender parameter, JdbcType jdbcType)
			throws SQLException {
		// baseTypeHandler已经帮我们做了parameter的null判断
		ps.setInt(i, parameter.getType());

	}

	/**
	 * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
	 * @param type 数据库中存储的自定义type属性
	 * @return type对应的枚举类
	 */
	private Gender locateGender(int type) {
		for(Gender gender : enums) {
			if(gender.getType() == type) {
				return gender;
			}
		}
		throw new IllegalArgumentException("未知的枚举类型：" + type + ",请核对" + Gender.getName());
	}

}
