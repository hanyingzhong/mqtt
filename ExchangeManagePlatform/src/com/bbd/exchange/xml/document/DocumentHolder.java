package com.bbd.exchange.xml.document;

import org.dom4j.Document;
/**
 * document 对象持有接口
 * @author Mao
 *
 */
public interface DocumentHolder {

	/**
	 * 根据路径返回document对象
	 * @param filePath
	 * @return
	 */
	public Document getDocument(String filePath);
}
