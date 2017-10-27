package com.bbd.exchange.test;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientSimulation {

	// �õ���ʾ����Ļ�Ŀ��
	static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	// ���崰��Ŀ��
	static int windowsWedth = 600;
	static int windowsHeight = 600;

	static String password = "";
	
	public static void main(String[] args) {
		// ���� JFrame ʵ��
		JFrame frame = new JFrame("Login Example");
		// Setting the width and height of frame
		frame.setSize(350, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds((width - windowsWedth) / 2, (height - windowsHeight) / 2, windowsWedth, windowsHeight);

		/*
		 * ������壬��������� HTML �� div ��ǩ ���ǿ��Դ��������岢�� JFrame ��ָ��λ�� ��������ǿ�������ı��ֶΣ���ť�����������
		 */
		JPanel panel = new JPanel();
		// ������
		frame.add(panel);
		/*
		 * �����û�����ķ����������������
		 */
		placeComponents(panel);

		// ���ý���ɼ�
		frame.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {

		Font font = new Font(null, Font.ITALIC, 24);

		/*
		 * ���ֲ���������߲��������� ������ò���Ϊ null
		 */
		panel.setLayout(null);

		// ���� JLabel
		JLabel userLabel = new JLabel("User:");
		/*
		 * ������������������λ�á� setBounds(x, y, width, height) x �� y ָ�����Ͻǵ���λ�ã��� width �� height
		 * ָ���µĴ�С��
		 */
		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);

		/*
		 * �����ı��������û�����
		 */
		JTextField userText = new JTextField(20);
		userText.setBounds(120, 20, 165, 30);
		userText.setFont(font);
		panel.add(userText);

		// ����������ı���
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 50, 80, 25);
		panel.add(passwordLabel);

		/*
		 * �����������������ı��� �����������Ϣ���Ե�Ŵ��棬���ڰ�������İ�ȫ��
		 */

		JTextField passwordText = new JTextField(20);
		passwordText.setBounds(120, 50, 165, 30);
		passwordText.setFont(font);
		panel.add(passwordText);

		// ������¼��ť
		JButton loginButton = new JButton("login");
		loginButton.setBounds(10, 300, 80, 25);
		panel.add(loginButton);

		placeSubscribeTopic(panel, font);
		placePublishTopic(panel, font);
		
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// �����Ի���
				JOptionPane.showMessageDialog(null, "�����Ի���");
			}
		});
	}

	
	private static void placeSubscribeTopic(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Subscribe Topic:");
		passwordLabel.setBounds(10, 100, 100, 25);
		panel.add(passwordLabel);

		JTextField passwordText = new JTextField(20);
		passwordText.setBounds(120, 100, 165, 30);
		passwordText.setFont(font);
		panel.add(passwordText);	
	}
	
	private static void placePublishTopic(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Publish Topic:");
		passwordLabel.setBounds(10, 140, 100, 25);
		panel.add(passwordLabel);

		JTextField passwordText = new JTextField(20);
		passwordText.setBounds(120, 140, 165, 30);
		passwordText.setFont(font);
		panel.add(passwordText);	
	}
}