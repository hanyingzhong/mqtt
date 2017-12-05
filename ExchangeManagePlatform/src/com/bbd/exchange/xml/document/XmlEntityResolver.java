package com.bbd.exchange.xml.document;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * 指定xml验证的dtd
 * @author Mao
 *
 */
public class XmlEntityResolver implements EntityResolver {

	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		if ("-//RONGDI//DTD BEAN//CN".equals(publicId)&&"http://www.cnblogs.com/rongdi/beans.dtd".equals(systemId)) {
            InputStream stream = this.getClass().
            getResourceAsStream("/com/bbd/exchange/xml/document/beans.dtd");
            return new InputSource(stream);
        }
		return null;
	}

}
