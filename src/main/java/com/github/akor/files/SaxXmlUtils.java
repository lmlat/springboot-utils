package com.github.akor.files;

import com.github.akor.common.PathUtils;
import com.github.akor.model.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Company 11bee
 * @Author : tao.ai
 * @Create : 2021/09/30 12:21
 * @Description :
 */
@Deprecated
public class SaxXmlUtils {
    static class XmlParseHandler extends DefaultHandler {

        private List<User> users;
        private String currentTag; // 记录当前解析到的节点名称
        private User user; // 记录当前的user

        /**
         * 文档解析结束后调用
         */
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        /**
         * 节点解析结束后调用
         *
         * @param uri       : 命名空间的uri
         * @param localName : 标签的名称
         * @param qName     : 带命名空间的标签名称
         */
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            super.endElement(uri, localName, qName);
            if ("user".equals(localName)) {
                users.add(user);
                user = null;
            }
            currentTag = null;
        }

        /**
         * 文档解析开始调用
         */
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            users = new ArrayList<User>();
        }

        /**
         * 节点解析开始调用
         *
         * @param uri       : 命名空间的uri
         * @param localName : 标签的名称
         * @param qName     : 带命名空间的标签名称
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if ("user".equals(localName)) { // 是一个用户
                for (int i = 0; i < attributes.getLength(); i++) {
                    user = new User();
                    if ("age".equals(attributes.getLocalName(i))) {
                        user.setAge(attributes.getValue(i));
                    }
                }
            }
            currentTag = localName; // 把当前标签记录下来
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String value = new String(ch, start, length); // 将当前TextNode转换为String
            if ("name".equals(currentTag)) {  // 当前标签为name标签，该标签无子标签，直接将上面获取到的标签的值封装到当前User对象中
                // 该节点为name节点
                user.setUsername(value);
            } else if ("password".equals(currentTag)) {  // 当前标签为password标签，该标签无子标签，直接将上面获取到的标签的值封装到当前User对象中
                // 该节点为password节点
                user.setPaaswod(value);
            }
        }

        public List<User> getUsers() {
            return users;
        }
    }

    static class CustomerResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            System.out.println("publicId:" + publicId);
            System.out.println("systemId:" + systemId);
            if (systemId.equals("")) {
                System.out.println("Resolving Entity...");
                return null;
            } else {
                // use the default behaviour
                return null;
            }
        }
    }

    static class CustomerErrorHandler implements ErrorHandler {
        @Override
        public void warning(SAXParseException exception) throws SAXException {
            System.out.println("SAXParseException warning:" + exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            System.out.println("SAXParseException error:" + exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            System.out.println("SAXParseException fatalError:" + exception);
        }
    }

    public static void main(String[] args) {
        //获取DocumentBuilderFactory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //设置是否需要校验xml格式
        documentBuilderFactory.setValidating(true);
        //设置是否需要校验命名空间
        documentBuilderFactory.setNamespaceAware(true);
        try {
            //获取DocumentBuilder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //设置entityResolver，解析entityResolver指定的xml
            documentBuilder.setEntityResolver(new CustomerResolver());
            //设置errorHandler，异常处理
            documentBuilder.setErrorHandler(new CustomerErrorHandler());

            //获取InputSource对象
            System.out.println(PathUtils.getClassPath("users.xml"));
            InputStream is = SaxXmlUtils.class.getClassLoader().getResourceAsStream("users.xml");
            //解析xml，获取Document对象
            Document document = documentBuilder.parse(new ByteArrayInputStream(toByteArray(is)));
            System.out.println(document);
            parseDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    private static void parseDocument(Document document) {
        //获取元素
        Element element = document.getDocumentElement();
        //获取孩子节点集合
        NodeList childNodes = element.getChildNodes();
        //遍历节点
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            //处理元素类型的节点
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                //用来存放元素节点中所有属性的key和value
                NamedNodeMap attributeMap = item.getAttributes();
                //根据指定名称获取属性值
                Node emailAttr = attributeMap.getNamedItem("email");
                if (emailAttr != null) {
                    System.out.println(emailAttr.getTextContent());
                }
                //循环子节点
                NodeList childNodes1 = item.getChildNodes();
                for (int j = 0; j < childNodes1.getLength(); j++) {
                    Node itemJ = childNodes1.item(j);
                    if (itemJ.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println(itemJ.getNodeName() + " " + itemJ.getFirstChild().getNodeValue());
                    }
                }
            }
        }
    }
}
