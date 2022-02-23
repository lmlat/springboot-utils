package com.aitao.files;

import com.aitao.common.Checks;
import com.aitao.model.User;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @Author : AiTao
 * @Create : 2021-12-02 18:28
 * @Description : Xml文件操作工具类(基于dom4j)
 */
public class XmlUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtils.class);

    /**
     * 获取对应节点的值（不含标签）(只适用单（一级）节点)。
     *
     * @param xmlPath xml文件路径
     * @param name    节点名
     * @return 节点的值（不含标签），如果节点内包含子节点，返回空字符串。
     */
    public static String innerText(String xmlPath, String name) {
        String value = "";
        try {
            //解析dom
            Document doc = DocumentHelper.parseText(xmlPath);
            //获取根元素
            Element root = doc.getRootElement();
            //获取根节点迭代器进行节点遍历
            for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
                Element element = it.next();
                if (element.getName().equals(name)) {
                    value = element.getTextTrim();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取对应节点的内容（包含标签）(只适用单（一级）节点)。
     *
     * @param xmlPath xml文件路径
     * @param name    节点名
     * @return 节点的xml内容（包含标签）
     */
    public static String innerXML(String xmlPath, String name) {
        String value = "";
        try {
            Document doc = DocumentHelper.parseText(xmlPath);
            Element root = doc.getRootElement();
            for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
                Element element = it.next();
                if (element.getName().equals(name)) {
                    value = element.asXML();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取根节点
     *
     * @param document XML文档模型
     * @return {@link Element 根节点元素}
     */
    public static Element root(Document document) {
        return Objects.nonNull(document) ? document.getRootElement() : null;
    }

    public static Element root(String xmlPath) {
        return root(new File(xmlPath), "utf-8");
    }

    public static Element root(File file, String charset) {
        Document document = parse(file, charset);
        return Objects.nonNull(document) ? document.getRootElement() : null;
    }

    /**
     * 获取指定节点的文本值，若节点不存在则返回缺少值defaultValue
     *
     * @param element      节点对象
     * @param defaultValue 缺省值
     * @return {@link String 节点元素值}
     */
    public static String value(Element element, String defaultValue) {
        return Objects.isNull(element) ? (Objects.nonNull(defaultValue) ? defaultValue : null) : element.getTextTrim();
    }

    /**
     * 获取节点的值
     *
     * @param name          节点名称
     * @param parentElement 父节点
     * @return {@link String 节点元素值}
     */
    public static String value(String name, Element parentElement) {
        if (Objects.isNull(parentElement)) {
            return null;
        } else {
            Element element = parentElement.element(name);
            if (Objects.nonNull(element)) {
                return element.getTextTrim();
            }
        }
        throw new RuntimeException("找不到节点" + name);
    }

    /**
     * 获取节点的文本值
     *
     * @param element 节点对象
     * @return {@link String 节点元素值}
     */
    public static String value(Element element) {
        return value(element, null);
    }


    public static Document findCDATA(Document body, String path) {
        return stringToXml(value(path, root(body)));
    }

    /**
     * 读取文档
     *
     * @param xmlPath xml文件路径
     * @return {@link Document 得到xml文档模型对象}
     */
    public static Document parse(String xmlPath) {
        return parse(new File(xmlPath));
    }

    /**
     * 读取文档
     *
     * @param xmlFile xml格式文件
     * @return {@link Document 得到xml文档模型对象}
     */
    public static Document parse(File xmlFile) {
        return parse(xmlFile, "utf-8");
    }

    /**
     * 读取文档
     *
     * @param xmlFile xml格式文件
     * @param charset 字符编码
     * @return {@link Document 得到xml文档模型对象}
     */
    public static Document parse(File xmlFile, String charset) {
        if (Objects.isNull(xmlFile)) {
            throw new NullPointerException("文件对象为空 null");
        }
        if (!xmlFile.exists()) {
            throw new RuntimeException("系统找不到指定目录文件, path:" + xmlFile);
        }
        //定义sax对象
        SAXReader reader = new SAXReader();
        //设置文件编码
        if (Checks.isNotEmpty(charset)) {
            reader.setEncoding(charset);
        }
        Document document = null;
        try {
            //读取指定文件并生成文档模型对象
            document = reader.read(xmlFile);
        } catch (DocumentException e) {
            LOGGER.error("读取文件异常, 文件路径:{}", xmlFile.getPath(), e);
        }
        return document;
    }

    /**
     * 读取文档
     *
     * @param url xml文件URL地址
     * @return {@link Document 得到xml文档模型对象}
     */
    public static Document parse(URL url) {
        return parse(url, null);
    }

    /**
     * 读取文档
     *
     * @param url     xml文件URL地址
     * @param charset 字符编码
     * @return {@link Document 得到xml文档模型对象}
     */
    public static Document parse(URL url, String charset) {
        if (Objects.isNull(url)) {
            return null;
        }
        //创建sax解析器
        SAXReader reader = new SAXReader();
        //设置编码
        if (Checks.isNotEmpty(charset)) {
            reader.setEncoding(charset);
        }
        Document document = null;
        try {
            //读取指定文件并生成文档模型对象
            document = reader.read(url);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }


    /**
     * 字符串转换成xml
     *
     * @param text
     * @return
     */
    public static Document stringToXml(String text) {
        try {
            return DocumentHelper.parseText(text);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将文档树转换成字符串
     *
     * @param doc
     * @return
     */
    public static String toString(Document doc) {
        return toString(doc, null);
    }

    /**
     * 将xml格式文件转换成字符串表示
     *
     * @param document 文档模型对象
     * @param charset  字符编码
     * @return 转换成字符串
     */
    public static String toString(Document document, String charset) {
        if (Objects.isNull(document)) {
            return "";
        }
        if (Objects.isNull(charset)) {
            return document.asXML();
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        StringWriter strWriter = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(strWriter, format);
        try {
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strWriter.toString();
    }

    /**
     * 持久化文件
     *
     * @param document XML文档模型对象
     * @param filePath 文件保存路径
     * @param charset  字符编码
     */
    public static void save(Document document, String filePath, String charset) {
        if (Objects.isNull(document)) {
            throw new NullPointerException("文档模型对象不能为空");
        }
        if (Objects.isNull(charset)) {
            throw new NullPointerException("字符编码不能不为空");
        }
        //优化打印结果
        OutputFormat format = OutputFormat.createPrettyPrint();
        //设置编码
        format.setEncoding(charset);
        try (FileOutputStream fos = new FileOutputStream(filePath); OutputStreamWriter osw = new OutputStreamWriter(fos, charset)) {
            //XML文件写入器
            XMLWriter xmlWriter = new XMLWriter(osw, format);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            LOGGER.error("持久化保存文件异常", e);
        }
    }

    /**
     * 修改xml节点的值
     *
     * @param xmlFile       原xml文件
     * @param nodes         要修改的节点
     * @param attributeName 属性名称
     * @param newValue      新值
     * @param outputPath    输出文件路径及文件名 如果输出文件为null，则默认为原xml文件
     */
    public static void update(File xmlFile, String nodes, String attributeName, String newValue, String outputPath) {
        try {
            Document document = parse(xmlFile);
            //xpath获取指定节点
            List<Node> list = document.selectNodes(nodes);
            for (Node node : list) {
                Attribute attribute = (Attribute) node;
                if (attribute.getName().equals(attributeName)) {
                    attribute.setValue(newValue);
                }
            }
            XMLWriter outputFile;
            if (Checks.isNotEmpty(outputPath)) {
                outputFile = new XMLWriter(new FileWriter(outputPath));
            } else { //输出文件为原文件
                outputFile = new XMLWriter(new FileWriter(xmlFile));
            }
            outputFile.write(document);
            outputFile.close();
        } catch (IOException e) {
            LOGGER.error("节点元素值更新异常", e);
        }
    }

    /**
     * 序列化对象成XML文件，基于XMLEncoder
     *
     * @param obj      对象实例
     * @param filename 输出文件名
     * @return
     */
    public static void serialize(Object obj, String filename) {
        try {
            if (filename == null || filename.isEmpty()) {
                filename = obj.getClass().getSimpleName();
            }
            File file = new File(filename);
            //初始化XMLEncoder(os, "UTF-8", true, 0); XML编译器
            XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
            xmlEncoder.writeObject(obj);
            xmlEncoder.close();
            LOGGER.info("序列化XML文件成功, 文件保存路径: {}", file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("序列化XML文件失败", e);
        }
    }

    /**
     * 反序列化对象，基于XMLDecoder来读取XMLEncoder输出的XML文件
     *
     * @param filename 文件名
     * @return 返回对象
     */
    public static Object deserialize(String filename) {
        Object obj = null;
        try {
            // 初始化XML解码器
            XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
            obj = xmlDecoder.readObject();
            xmlDecoder.close();
        } catch (IOException e) {
            LOGGER.error("解析XML文件失败", e);
        }
        return obj;
    }

    public static void main(String[] args) {
        // 构造一个StudentBean对象
        User user = new User();
        user.setUsername("aitao");
        user.setPaaswod("1234");
        user.setAge("18");
        //指定路径
        String fileName = "C:\\Users\\10034\\Desktop\\tgb_hz\\target\\classes\\User.xml";
        serialize(user, "User.xml");

        Object user2 = deserialize("User.xml");
        System.out.println(user2);
    }
}
