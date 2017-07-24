package com.shop.service;

import com.shop.core.exception.ParamException;
import com.shop.core.model.Attribute;
import com.shop.dao.AttributeDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by TW on 2017/6/13.
 */
@Service
public class AttributeService {
    @Autowired
    private AttributeDao attributeDao;

    public List<Attribute> findByCategoryId(Integer productCategoryId) {
        if(productCategoryId == null || productCategoryId < 1) {
            throw new ParamException("请选择分类");
        }

        List<Attribute> attributes = attributeDao.findByCategoryId(productCategoryId);
//        ["5英寸以下","5-6英寸","6-7英寸","7-8英寸","8-9英寸","9-10英寸","10英寸以上"]
        for (Attribute attribute : attributes) {
            String options = attribute.getOptions();
            if (StringUtils.isBlank(options)) {
                continue;
            }
            options = options.substring(options.indexOf("[") + 1, options.lastIndexOf("]"));
            List<String> optionList = new ArrayList<>();
            String[] optionArray = options.split(",");
            for (String option : optionArray) {
                optionList.add(option.substring(1, option.length() - 1));
            }
            attribute.setOptionsList(optionList.toArray(new String[]{})); // List.toArray(T[]) Aarrays.asList()
        }
        return attributes;
    }
}
