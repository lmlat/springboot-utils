package com.aitao.annotation.helper;

import com.aitao.annotation.IsDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author : AiTao
 * @Create : 2021-12-10 17:31
 * @Description :
 */
public class DateValidator implements ConstraintValidator<IsDate, String> {
    /**
     * 格式化字符串
     */
    private String formatString;

    /**
     * 是否比当前时间晚
     */
    private boolean future;

    /**
     * 是否比当前时间早
     */
    private boolean past;

    @Override
    public void initialize(IsDate constraintAnnotation) {
        formatString = constraintAnnotation.format();
        future = constraintAnnotation.future();
        past = constraintAnnotation.past();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Date date = parseDateStr(value, formatString);
        if (null == date) {
            return false;
        }
        Date now = new Date();
        if (future && date.before(now)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("必须比当前时间晚").addConstraintViolation();
            return false;
        }
        if (past && date.after(now)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("必须比当前时间早").addConstraintViolation();
            return false;
        }
        return true;
    }

    /**
     * 是否为日期字符串
     *
     * @param dateStr
     * @param formatString
     * @return
     */
    public static boolean isDateStr(String dateStr, String formatString) {
        return null != parseDateStr(dateStr, formatString);
    }

    /**
     * 转换日期字符串
     *
     * @param dateStr
     * @param formatString
     * @return
     */
    private static Date parseDateStr(String dateStr, String formatString) {
        if (null == dateStr || dateStr.length() <= 0) {
            return null;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatString);
            return format.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}
