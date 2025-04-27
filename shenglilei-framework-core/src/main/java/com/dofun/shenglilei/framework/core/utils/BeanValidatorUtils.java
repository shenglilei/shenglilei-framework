package com.dofun.shenglilei.framework.core.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Slf4j
public class BeanValidatorUtils {

    /**
     * 通过bindingResult构建错误信息
     */
    public static String getErrorMsg(BindingResult bindingResult) {
        List<ObjectError> errors = bindingResult.getAllErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (int i = 0; i < errors.size(); i++) {
            ObjectError error = errors.get(i);
            if (i != 0) {
                errorMsg.append(",");
            }
            errorMsg.append(error.getDefaultMessage());
        }
        return errorMsg.toString();
    }

}