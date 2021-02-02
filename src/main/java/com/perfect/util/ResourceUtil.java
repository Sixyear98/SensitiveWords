package com.perfect.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @Description 资源相关
 *
 * @author Gaonan
 */
public class ResourceUtil {

	private static ClassLoader loader = Thread.currentThread().getContextClassLoader();

	private static Charset charset;

	
	private ResourceUtil() {

	}

	/**
	 * 资源URL
	 *
	 * @param resource
	 * @return
	 */
	public static URL getUrl(String resource) {
		URL url = loader.getResource(resource);
		if (url == null) {
			url = loader.getResource(File.separator + resource);
		}
		return url;
	}

	/**
	 * 资源字节流---需要手动关闭字节流
	 *
	 * @param resource
	 * @return
	 */
	public static InputStream getStream(String resource) {
		InputStream stream = loader.getResourceAsStream(resource);
		if (null == stream) {
			stream = loader.getResourceAsStream("/" + resource);
		}
		return stream;
	}

	/**
	 * 资源字符流---需要手动关闭字符流
	 *
	 * @param resource
	 * @return
	 */
	public static Reader getReader(String resource) {
		Reader reader = null;
		InputStream stream = getStream(resource);
		if (charset == null) {
			reader = new InputStreamReader(stream);
		} else {
			reader = new InputStreamReader(stream, charset);
		}
		return reader;
	}
	
	/**
	 * 资源配置数据
	 *
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static Properties getProperties(String resource) throws IOException {
		Properties properties = new Properties();
		InputStream stream = getStream(resource);
		properties.load(stream);
		stream.close();
		return properties;
	}
	
	/**
	 * XML资源配置数据
	 *
	 * @param resource
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static Document getDocument(String resource) throws IOException, ParserConfigurationException, SAXException {
		InputStream stream = getStream(resource);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(stream);
		return document;
	}
	
	/**
	 * XML资源配置数据
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static Document getDocument(File file) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		return document;
	}
	
	/**
	 * 返回资源文件
	 *
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static File getFile(String resource) throws IOException {
		URL url = getUrl(resource);
		String path = url.getFile();
		String tempPath = URLDecoder.decode(path, "UTF-8");
		File file = new File(tempPath);
		return file;
	}

	/**
	 * 查找类
	 *
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> findClass(String className) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(className, true, loader);
		return clazz;
	}

	/**
	 * 字节符集
	 *
	 * @param charset
	 */
	public static void updateCharset(Charset charset) {
		charset = charset;
	}

}
