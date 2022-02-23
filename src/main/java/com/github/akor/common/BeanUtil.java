package com.github.akor.common;

import com.github.akor.model.BeanAttributeInfo;
import com.github.akor.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/18 15:28
 * @Description : Bean工具类
 */
public class BeanUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);
    //记录属性字段
    public static Map<String, Map<String, BeanAttributeInfo>> BEAN_SIMPLE_PROPERTIES;
    //记录属性字段(忽略大小写)
    public static Map<String, Map<String, BeanAttributeInfo>> BEAN_SIMPLE_IGNORE_PROPERTIES;

    static {
        BEAN_SIMPLE_PROPERTIES = new ConcurrentHashMap<>();
        BEAN_SIMPLE_IGNORE_PROPERTIES = new ConcurrentHashMap<>();
    }

    /**
     * 校验className类中的filedName字段是否是被public修饰
     *
     * @param className 类型名
     * @param filedName 字段名
     * @return true or false
     */
    public static boolean isPublicFiled(String className, String filedName) {
        try {
            return Modifier.isPublic(Class.forName(className).getDeclaredField(filedName).getModifiers());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 校验className类中的filedName字段是否是被protected修饰
     *
     * @param className 类型名
     * @param filedName 字段名
     * @return true or false
     */
    public static boolean isProtectedField(String className, String filedName) {
        try {
            return Modifier.isProtected(Class.forName(className).getDeclaredField(filedName).getModifiers());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验className类中的filedName字段是否是被private修饰
     *
     * @param className 类型名
     * @param filedName 字段名
     * @return true or false
     */
    public static boolean isPrivateField(String className, String filedName) {
        try {
            return Modifier.isPrivate(Class.forName(className).getDeclaredField(filedName).getModifiers());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 拷贝同名属性
     *
     * @param srcBean  源对象
     * @param destBean 目标对象
     */
    public static void copyProperties(Object srcBean, Object destBean) {
        try {
            //解析bean对象
            resolveBean(srcBean.getClass());
            resolveBean(destBean.getClass());
            Map<String, BeanAttributeInfo> srcMap = BEAN_SIMPLE_PROPERTIES.get(srcBean.getClass().getName());
            Map<String, BeanAttributeInfo> destMap = BEAN_SIMPLE_PROPERTIES.get(destBean.getClass().getName());
            //求两个map交集
            Map<String, BeanAttributeInfo> newMap = CollectionUtils.intersect(srcMap, destMap);
            for (String key : newMap.keySet()) {
                BeanAttributeInfo src = BEAN_SIMPLE_PROPERTIES.get(srcBean.getClass().getName()).get(key);
                BeanAttributeInfo dest = BEAN_SIMPLE_PROPERTIES.get(destBean.getClass().getName()).get(key);
                //执行getter、setter方法完成赋值操作
                dest.getSetMethod().invoke(destBean, src.getGetMethod().invoke(srcBean));
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析bean对象,并获取对应的信息
     *
     * @param clazz Bean实例的类对象
     */
    private static void resolveBean(Class<?> clazz) throws IntrospectionException {
        String className = clazz.getName();
        Map<String, BeanAttributeInfo> currentBean = BEAN_SIMPLE_PROPERTIES.get(className);
        if (currentBean == null || currentBean.isEmpty()) {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null && propertyDescriptors.length > 0) {
                Map<String, BeanAttributeInfo> beanSimpleProperties = new ConcurrentHashMap<>();
                Map<String, BeanAttributeInfo> beanSimpleIgnoreProperties = new ConcurrentHashMap<>();
                Arrays.stream(propertyDescriptors).filter(pd -> !pd.getName().equals("class")).forEach(pd -> {
                    String fieldName = pd.getName();
                    BeanAttributeInfo info = new BeanAttributeInfo(fieldName, pd.getPropertyType(), pd.getReadMethod(), pd.getWriteMethod(), isPublicFiled(className, fieldName), isProtectedField(className, fieldName), isPrivateField(className, fieldName));
                    beanSimpleProperties.put(fieldName, info);
                    beanSimpleIgnoreProperties.put(fieldName.toLowerCase(), info);
                });
                BEAN_SIMPLE_PROPERTIES.put(className, beanSimpleProperties);
                BEAN_SIMPLE_IGNORE_PROPERTIES.put(className, beanSimpleIgnoreProperties);
            }
        }
    }

    /**
     * 对象转换Map
     *
     * @param obj 对象
     * @return 返回Map集合
     */
    private static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            if (obj != null) {
                Class<?> clazz = obj.getClass();//获取当前对象的类
                List<String> ignoreField = Arrays.asList("serialVersionUID", "LOGGER");
                Field[] fields = getFields(clazz);//获取该类所有的字段信息
                if (fields != null) {
                    for (Field field : fields) {
                        if (ignoreField.contains(field.getName())) {//忽略serialVersionUID 和 logger
                            continue;
                        }
                        field.setAccessible(true);
                        map.put(field.getName(), field.get(obj));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("对象数据异常", e);
        }
        return map;
    }

    /**
     * 获取指定类对象中的所有字段
     *
     * @param clazz 类对象
     * @return 返回字段集合
     */
    public static Field[] getFields(Class<?> clazz) {
        if (clazz == Object.class) { // 获取父类
            return null;
        }
        return combineFields(getFields(clazz.getSuperclass()), clazz.getDeclaredFields());
    }

    /**
     * 合并2个Filed数组
     *
     * @param f1 字段数组1
     * @param f2 字段数组2
     * @return 返回合并后的字段集合
     */
    public static Field[] combineFields(Field[] f1, Field[] f2) {
        if (f1 == null && f2 == null) {
            return null;
        } else if (f2 == null) {
            return f1;
        } else if (f1 == null) {
            return f2;
        }
        int length = f1.length + f2.length;
        Field[] fields = new Field[length];
        int i = 0;
        for (Field field : f1) {
            fields[i++] = field;
        }
        for (Field field : f2) {
            fields[i++] = field;
        }
        return fields;
    }

    public static void main(String[] args) {
        Information user2 = new Information();
        user2.setSex("男");
//        User user = new User("aitao", "123456");

        Map<String, Object> map = toMap(user2);
        System.out.println(map);

/*        User user1 = new User();
        Information user2 = new Information();
        copyProperties(user, user1);
        copyProperties(user, user2);
        System.out.println(user1);
        System.out.println(user2);*/

    }

    static class Information extends User {
        private String username;
        private String password;
        private String age;
        private String sex;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "Information{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", age='" + age + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }
}
