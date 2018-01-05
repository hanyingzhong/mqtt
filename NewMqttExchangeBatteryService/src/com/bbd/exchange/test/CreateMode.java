package com.bbd.exchange.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.service.ExchangeServiceResponseMessage;

public enum CreateMode {
	   
    /**
     * �־ýڵ㣺�ڵ㴴���󣬻�һֱ���ڣ�������ͻ��˻ỰʧЧ��ɾ����
     */
    PERSISTENT (0, false, false),

    /**
    * �־�˳��ڵ㣺����������־ýڵ�һ�£������ڵ�Ĺ����У�zookeeper���������ֺ��Զ�׷��һ���������������ֺ�׺����Ϊ�µĽڵ����� 
    */
    PERSISTENT_SEQUENTIAL (2, false, true),

    /**
     *  ��ʱ�ڵ㣺�ͻ��˻ỰʧЧ�����ӹرպ󣬸ýڵ�ᱻ�Զ�ɾ�����Ҳ�������ʱ�ڵ����洴���ӽڵ㣬�������´�org.apache.zookeeper.KeeperException$NoChildrenForEphemeralsException��
     */
    EPHEMERAL (1, true, false),

    /**
     * ��ʱ˳��ڵ㣺������������ʱ�ڵ�һ�£������ڵ�Ĺ����У�zookeeper���������ֺ��Զ�׷��һ���������������ֺ�׺����Ϊ�µĽڵ����� 
     */
    EPHEMERAL_SEQUENTIAL (3, true, true);
	private static final Logger logger = LoggerFactory.getLogger(ExchangeServiceResponseMessage.class);
    private boolean ephemeral;
    private boolean sequential;
    private int flag;
    CreateMode(int flag, boolean ephemeral, boolean sequential) {
        this.flag = flag;
        this.ephemeral = ephemeral;
        this.sequential = sequential;
    }
    public boolean isEphemeral() {
        return ephemeral;
    }
    public boolean isSequential() {
        return sequential;
    }
    public int toFlag() {
        return flag;
    }
    static public CreateMode fromFlag(int flag) /*throws KeeperException*/ {
        switch(flag) {
        case 0: return CreateMode.PERSISTENT;
        case 1: return CreateMode.EPHEMERAL;
        case 2: return CreateMode.PERSISTENT_SEQUENTIAL;
        case 3: return CreateMode.EPHEMERAL_SEQUENTIAL ;
        default:
        	logger.error("Received an invalid flag value to convert to a CreateMode");
            //throw new KeeperException.BadArgumentsException();
        	return CreateMode.PERSISTENT;
        }
    }
}
