package com.meitianhui.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * xml报文工具类
 * 
 * @author Tiny
 * 
 */
public class XmlUtil {

	/**
	 * url请求的参数转MAP
	 * 
	 * @param queryString
	 *            请求的参数串
	 * @param enc
	 *            编码
	 * @return
	 */
	public static Map<String, String> getRequestParam(String queryString) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}

				int index = subStr.indexOf("=");
				param = subStr.substring(0, index);
				value = subStr.length() == index + 1 ? "" : subStr.substring(index + 1);
				paramsMap.put(param, value);
			} while (ampersandIndex > 0);
		}
		return paramsMap;
	}

	/**
	 * url请求的参数转MAP
	 * 
	 * @param queryString
	 *            请求的参数串
	 * @param enc
	 *            编码
	 * @return
	 */
	public static Map<String, String> getParamsMap(String queryString, String enc) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}

				int index = subStr.indexOf("=");
				param = subStr.substring(0, index);
				value = subStr.length() == index + 1 ? "" : subStr.substring(index + 1);
				try {
					value = URLDecoder.decode(value, enc);
					paramsMap.put(param, value);
				} catch (UnsupportedEncodingException ignored) {
					return null;
				}
			} while (ampersandIndex > 0);
		} else {
			return null;
		}

		return paramsMap;
	}

	/**
	 * 替换xml中在某个字符串之后节点的值，只适合替换报文中只有一个指定名字的节点
	 * 
	 * @param nodeStart
	 *            节点开始标签 eg :<TransactionID>
	 * @param nodeEnd
	 *            节点结束标签 eg :</TransactionID>
	 * @param relacement
	 *            节点替换的内容
	 * @param src
	 *            原字符串
	 * @return
	 */
	public static String relaceNodeContentAfter(String nodeStart, String nodeEnd, String afterStart, String relacement,
			String src) {
		int nodeStartLength = nodeStart.length();
		int startAfter = src.indexOf(afterStart);
		int start = src.indexOf(nodeStart, startAfter);
		int end = src.indexOf(nodeEnd, startAfter);

		if (start > -1 && end > -1) {
			String segStart = src.substring(0, start + nodeStartLength);
			String segEnd = src.substring(end, src.length());
			return segStart + relacement + segEnd;
		}
		return src;
	}

	/**
	 * 替换xml中节点的值，只适合替换报文中只有一个指定名字的节点
	 * 
	 * @param nodeStart
	 *            节点开始标签 eg :<TransactionID>
	 * @param nodeEnd
	 *            节点结束标签 eg :</TransactionID>
	 * @param relacement
	 *            节点替换的内容
	 * @param src
	 *            原字符串
	 * @return
	 */
	public static String relaceNodeContent(String nodeStart, String nodeEnd, String relacement, String src) {
		int nodeStartLength = nodeStart.length();
		int start = src.indexOf(nodeStart);
		int end = src.indexOf(nodeEnd);

		if (start > -1 && end > -1) {
			String segStart = src.substring(0, start + nodeStartLength);
			String segEnd = src.substring(end, src.length());
			return segStart + relacement + segEnd;
		}
		return src;
	}

	/**
	 * 获取xml文本中节点的值
	 * 
	 * @param nodeStart
	 *            节点开始标签 eg :<TransactionID>
	 * @param nodeEnd
	 *            节点结束标签 eg :</TransactionID>
	 * @param src
	 *            原字符串
	 * @return
	 */
	public static String parseNodeValueFromXml(String nodeStart, String nodeEnd, String src) {
		int nodeStartLength = nodeStart.length();
		int start = src.indexOf(nodeStart);
		int end = src.indexOf(nodeEnd);
		if (start > -1 && end > -1) {
			String nodeVal = src.substring(start + nodeStartLength, end);
			return nodeVal;
		}
		return "";
	}

	/**
	 * 获取xml中xpath节点的值
	 * 
	 * @param xml
	 * @param encoding
	 * @param xpath
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	public static String getNodeValFromXml(String xml, String encoding, String xpath)
			throws UnsupportedEncodingException, DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new ByteArrayInputStream(xml.getBytes(encoding)));
		return document.selectSingleNode(xpath).getText();
	}

	/**
	 * 获取xml中xpath节点的值
	 * 
	 * @param in
	 * @param xpath
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	public static String getNodeValFromStream(InputStream in, String xpath)
			throws UnsupportedEncodingException, DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);
		return document.selectSingleNode(xpath).getText();
	}

	/**
	 * 替换节点中内容
	 * 
	 * @param msg
	 * @param elementName
	 * @param value
	 * @return
	 */
	public static String replaceXmlContent(String msg, String elementName, String value) {
		int begin = msg.indexOf("<" + elementName + ">");
		int end = msg.indexOf("</" + elementName + ">", begin + elementName.length());
		if (begin > -1 && end > -1) {
			return msg.replace(msg.substring(begin, end), "<" + elementName + ">" + value);
		}
		return msg;
	}

	/**
	 * 支付结果通知查询的返回信息是XML格式的，需要转换成Map
	 * 
	 * @param xmlStr
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, String> switchXml2Map(String xmlStr) {
		Map<String, String> responseMap = new HashMap<String, String>();
		try {
			Document doc = DocumentHelper.parseText(xmlStr);
			Element rootE = doc.getRootElement();
			putData(rootE, responseMap);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return responseMap;
	}

	/**
	 * 
	 * @param e
	 * @param map
	 */
	private static void putData(Element e, Map<String, String> map) {
		if (e == null) {
			return;
		}
		for (Object element : e.elements()) {
			Element e1 = (Element) element;
			if (!e1.elements().isEmpty()) {
				putData(e1, map);
			} else if (!e1.attributes().isEmpty()) {
				map.put(e1.attribute(0).getValue().trim(), e1.getTextTrim());
			} else {
				map.put(e1.getName().trim(), e1.getTextTrim());
			}
		}
	}

}
