package com.bbd.exchange.test;

import java.util.WeakHashMap;

public class WeakHashMapTest {
	public static void main(String[] args) {
		WeakHashMap w = new WeakHashMap();
		// ����key-value�е�key ������������û��ǿ����ָ���ʵ�ʶ���
		w.put(new String("����"), new String("����"));
		w.put(new String("��ѧ"), new String("����"));
		w.put(new String("Ӣ��"), new String("�е�"));
		// ����һ���ַ�����ǿ����
		w.put("java", new String("�ر�����"));
		System.out.println(w);
		// ֪ͨ�������ջ��������л���
		System.gc();
		System.runFinalization();
		// �ٴ����w
		System.out.println("�ڶ������:" + w);
	}
}
