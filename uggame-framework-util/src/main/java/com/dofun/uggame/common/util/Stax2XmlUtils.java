package com.dofun.uggame.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 描述：java对象与soapXml互相转换
 */
public class Stax2XmlUtils {

    /**
     * soap请求参数 头信息
     *
     * @param prefix
     * @return
     */
    public static String getHeader(String prefix, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:" + prefix + "=\"" + name + "\">");
        sb.append("<soapenv:Header/>");
        sb.append("<soapenv:Body>");

        return sb.toString();
    }

    /**
     * soap请求参数 结尾信息
     *
     * @return
     */
    public static String getEnding() {
        StringBuilder sb = new StringBuilder();
        sb.append("</soapenv:Body>");
        sb.append("</soapenv:Envelope>");

        return sb.toString();
    }

    /**
     * 解析实体对象转换成soap Xml
     *
     * @param t      解析对象
     * @param sb     返回结果
     * @param prefix soap前缀
     * @throws Exception
     */
    public static void getAnalysisEntity(Object t, StringBuilder sb, String prefix, String entityPackage) throws Exception {
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = JniInvokeUtils.getClass(t).getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            // 获取属性的名字
            String initName = fields[i].getName();
            // 将属性的首字符大写，方便构造get，set方法
            String name = initName.substring(0, 1).toUpperCase() + initName.substring(1);
            // 获取属性的类型
            String type = fields[i].getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            // String 类型处理
            if (type.equals("class java.lang.String")) {
                // 调用getter方法获取属性值
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                String value = (String) m.invoke(t);
                if (value != null) {
                    sb.append("<" + prefix + ":" + initName + ">" + value + "</" + prefix + ":" + initName + ">");
                }
            }
            // 日期 类型处理
            if (type.equals("class javax.xml.datatype.XMLGregorianCalendar")) {
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                XMLGregorianCalendar value = (XMLGregorianCalendar) m.invoke(t);
                if (value != null) {
                    sb.append("<" + prefix + ":" + initName + ">" + value.toString().substring(0, 19) + "</" + prefix + ":" + initName + ">");
                }
            }
            // Double 类型处理
            if (type.equals("class java.lang.Double")) {
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                Double value = (Double) m.invoke(t);
                if (value != null) {
                    sb.append("<" + prefix + ":" + initName + ">" + value + "</" + prefix + ":" + initName + ">");
                }
            }
            // Integer 类型处理
            if (type.equals("class java.lang.Integer")) {
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                Integer value = (Integer) m.invoke(t);
                if (value != null) {
                    sb.append("<" + prefix + ":" + initName + ">" + value + "</" + prefix + ":" + initName + ">");
                }
            }
            // Boolean 类型处理
            if (type.equals("class java.lang.Boolean")) {
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                Boolean value = (Boolean) m.invoke(t);
                if (value != null) {
                    sb.append("<" + prefix + ":" + initName + ">" + value + "</" + prefix + ":" + initName + ">");
                }
            }
            // 对象 类型处理
            if (type.contains(entityPackage)) {
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                Object value = (Object) m.invoke(t);
                if (value != null) {
                    sb.append("<" + prefix + ":" + initName + ">");
                    getAnalysisEntity(value, sb, prefix, entityPackage);
                    sb.append("</" + prefix + ":" + initName + ">");
                }
            }
            // List 类型处理
            if (type.equals("java.util.List<java.lang.String>")) {
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                List value = (List) m.invoke(t);
                if (value != null) {
                    sb.append("<" + prefix + ":" + initName + ">" + StringUtils.strip(value.toString(), "[]") + "</" + prefix + ":" + initName + ">");
                }
            } else if (type.length() >= 14 && type.substring(0, 14).equals("java.util.List")) {
                Method m = JniInvokeUtils.getClass(t).getMethod("get" + name);
                List value = (List) m.invoke(t);
                if (value != null) {
                    for (Object o : value) {
                        if (o != null) {
                            sb.append("<" + prefix + ":" + initName + ">");
                            getAnalysisEntity(o, sb, prefix, entityPackage);
                            sb.append("</" + prefix + ":" + initName + ">");
                        }
                    }
                }
            }

        }
    }

    /**
     * 封装XML 对象转换成SOAP XML
     *
     * @param t
     * @param prefix
     * @return
     * @throws Exception
     */
    public static String beanToSoapXml(Object t, String name, String prefix, String entityPackage) throws Exception {
        // 封装请求消息 bean to xml
        StringBuilder sb = new StringBuilder();
        // 封装请求头
        sb.append(Stax2XmlUtils.getHeader(prefix, name));
        // 获取对象本身名称
        String objectName = JniInvokeUtils.getClass(t).getSimpleName();
        sb.append("<" + prefix + ":" + objectName + ">");
        // 解析实体
        getAnalysisEntity(t, sb, prefix, entityPackage);
        sb.append("</" + prefix + ":" + objectName + ">");
        // 封装请求尾
        sb.append(Stax2XmlUtils.getEnding());

        return sb.toString();
    }

    /**
     * 封装XML 对象转换 XML
     *
     * @param bean
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> String beanToXml(T bean) throws Exception {
        XmlMapper mapper = new XmlMapper();
        mapper.setDefaultUseWrapper(false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(format);
        String string = mapper.writeValueAsString(bean);
        return string;
    }


    /**
     * 解析XML 转换成对象
     *
     * @param xmlStr
     * @return
     * @throws Exception
     */
    public static <T> T xmlToBean(String xmlStr, Class<T> cls) throws Exception {
        XMLInputFactory2 factory2 = (XMLInputFactory2) XMLInputFactory2.newInstance();
        XMLStreamReader2 stream = (XMLStreamReader2) factory2.createXMLStreamReader(new BufferedReader(new StringReader(xmlStr)));
        XmlMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setDefaultUseWrapper(false);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(format);
        T obj = mapper.readValue(stream, cls);
        return obj;
    }

}
