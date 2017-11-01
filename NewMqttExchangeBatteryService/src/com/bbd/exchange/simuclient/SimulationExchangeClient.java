package com.bbd.exchange.simuclient;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bbd.exchange.mobile.ClientRequestMessage;

public class SimulationExchangeClient extends JFrame {
	// 得到显示器屏幕的宽高
	static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	// 定义窗体的宽高
	static int windowsWedth = 600;
	static int windowsHeight = 600;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ExchangeMqttClient mqttClient;

	static JTextField requestTypeText;
	/*
	 * store the request user info.......
	 * */
	static JTextField clientIdText;
	/*
	 * store the cabinetID info.......
	 * */
	static JTextField cabinetIdText;
	/*
	 * store the batteryType info.......
	 * */
	static JTextField batteryTypeText;
	
	
	public SimulationExchangeClient() {
		this.setSize(350, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds((width - windowsWedth) / 2, (height - windowsHeight) / 2, windowsWedth, windowsHeight);

		JPanel panel = new JPanel();
		// 添加面板
		add(panel);
		placeComponents(panel);
		setVisible(true);
	}

	private static void placeClientID(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Request ClientID:");
		passwordLabel.setBounds(10, 100, 150, 25);
		panel.add(passwordLabel);

		clientIdText = new JTextField(25);
		clientIdText.setBounds(150, 100, 250, 30);
		clientIdText.setFont(font);
		clientIdText.setText("18616996973-6973");
		panel.add(clientIdText);	
	}

	private static void placeCabinetID(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Exchange CabinetID:");
		passwordLabel.setBounds(10, 150, 150, 25);
		panel.add(passwordLabel);

		cabinetIdText = new JTextField(25);
		cabinetIdText.setBounds(150, 150, 250, 30);
		cabinetIdText.setFont(font);
		cabinetIdText.setText("HDG-00001238");
		panel.add(cabinetIdText);	
	}
	
	private static void placeBatteryType(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Battery Type:");
		passwordLabel.setBounds(10, 200, 150, 25);
		panel.add(passwordLabel);

		batteryTypeText = new JTextField(25);
		batteryTypeText.setBounds(150, 200, 250, 30);
		batteryTypeText.setFont(font);
		batteryTypeText.setText("60V20");
		panel.add(batteryTypeText);	
	}
	
	private static void placeComponents(JPanel panel) {

		Font font = new Font(null, Font.TRUETYPE_FONT, 24);

		/*
		 * 布局部分我们这边不多做介绍 这边设置布局为 null
		 */
		panel.setLayout(null);

		placeClientID(panel, font);
		placeCabinetID(panel, font);
		placeBatteryType(panel, font);
		
		// create exchange button
		JButton loginButton = new JButton("Exchange");
		loginButton.setBounds(150, 300, 80, 25);
		panel.add(loginButton);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientRequestMessage message = new ClientRequestMessage("exchange", clientIdText.getText(), cabinetIdText.getText(), batteryTypeText.getText());

				System.out.println(message.toString());
				
				if(false == mqttClient.client.isConnected()) {
					mqttClient.connect();				
				}			
				
				if(mqttClient.client.isConnected()) {
					mqttClient.sendPublish(message.getTopic(), message.toString());				
				}

				//JOptionPane.showMessageDialog(null, "弹出对话框" + cabinetIdText.getText());
			}
		});
	}

	public static void main(String[] args) {
		SimulationExchangeClient client = new SimulationExchangeClient();
		mqttClient = new ExchangeMqttClient("tcp://121.40.109.91", "parry","parry123", "DEV-1545662ER");
		
		mqttClient.connect();
	}
}
