package com.bbd.exchange.xml.document;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/**
 * 该类用来读取xml，解析成document
 * @author Mao
 *
 */
public class XmlDocumentHolder implements DocumentHolder {

	/**
	 * 用路径，document来存储多份xml信息
	 */
	private Map<String, Document> documents = new HashMap<>();
	
	@Override
	public Document getDocument(String filePath) {
		Document doc = this.documents.get(filePath);
		if(doc == null){
			doc = this.readDocument(filePath);
			this.documents.put(filePath, doc);
		}
		return doc;
	}

	private Document readDocument(String filePath) {
		try {
			SAXReader reader = new SAXReader(true);
			reader.setEntityResolver(new XmlEntityResolver());
			File xmlFile = new File(filePath);
			Document document = reader.read(xmlFile);
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
