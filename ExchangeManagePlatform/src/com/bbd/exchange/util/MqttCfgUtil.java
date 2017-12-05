package com.bbd.exchange.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Desc:properties�ļ���ȡ������
 * Created by hafiz.zhang on 2016/9/15.
 */
public class MqttCfgUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    public static void loadProps(){
        logger.info("��ʼ���� mqtt properties�ļ�����.......");
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("mqtt.properties");
            //in = PropertyUtil.class.getResourceAsStream("/jdbc.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("jdbc.properties�ļ�δ�ҵ�");
        } catch (IOException e) {
            logger.error("����IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("jdbc.properties�ļ����رճ����쳣");
            }
        }
        logger.info("����properties�ļ��������...........");
        logger.info("properties�ļ����ݣ�" + props);
    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
    
    public static String getServerUri() {
    	return getProperty("serveruri");
    }
    
    public static String getUsername() {
    	return getProperty("username");
    }
    
    public static String getPassword() {
    	return getProperty("password");
    }
}
