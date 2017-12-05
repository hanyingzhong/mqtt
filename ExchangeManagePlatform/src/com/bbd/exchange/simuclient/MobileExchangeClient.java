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

import com.bbd.exchange.message.ExchangeRequestMessage;
import com.bbd.exchange.message.MobileExchangeRequestMessage;
import com.bbd.exchange.platform.CommonClientMqttMsgCallback;
import com.bbd.exchange.platform.CommonExchangeMqttClient;
import com.bbd.exchange.util.MqttCfgUtil;

public class MobileExchangeClient extends JFrame {
	// �õ���ʾ����Ļ�Ŀ��
	static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	// ���崰��Ŀ��
	static int windowsWedth = 600;
	static int windowsHeight = 600;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static CommonExchangeMqttClient newMqttClient;

	static {
		newMqttClient = new CommonExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry", "parry123",
				"TEST-3245661222ER", null);

		try {
			newMqttClient.initClient(new CommonClientMqttMsgCallback(newMqttClient));
			newMqttClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		newMqttClient.sendSubscribe("exbattery/#");
	}
	
	static JTextField requestTypeText;
	/*
	 * store the request user info.......
	 */
	static JTextField clientIdText;
	/*
	 * store the cabinetID info.......
	 */
	static JTextField cabinetIdText;
	/*
	 * store the batteryType info.......
	 */
	static JTextField batteryTypeText;

	public MobileExchangeClient() {
		this.setSize(350, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds((width - windowsWedth) / 2, (height - windowsHeight) / 2, windowsWedth, windowsHeight);

		JPanel panel = new JPanel();
		// ������
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
		//cabinetIdText.setText("HDG-00001238");
		cabinetIdText.setText("EB000001");
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
		 * ���ֲ���������߲��������� ������ò���Ϊ null
		 */
		panel.setLayout(null);

		placeClientID(panel, font);
		placeCabinetID(panel, font);
		placeBatteryType(panel, font);
		
		placeNormalExchangeButtonType(panel, font);
	}
	
	private static void placeNormalExchangeButtonType(JPanel panel, Font font) {
		JButton notifyButton = new JButton("NewExchange");
		notifyButton.setBounds(150, 350, 150, 25);
		notifyButton.setFont(font);
		panel.add(notifyButton);

		notifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MobileExchangeRequestMessage mobileRequest = new MobileExchangeRequestMessage();
				
				mobileRequest.setBatteryType(batteryTypeText.getText());
				mobileRequest.setDeviceType("cabinet");
				mobileRequest.setDeviceID(cabinetIdText.getText());
				mobileRequest.setRequestID(clientIdText.getText());
				String exchange = mobileRequest.encode2Json();
				
				newMqttClient.sendPublish(mobileRequest.destinationTopic(), exchange);
				
/*				ExchangeRequestMessage message = new ExchangeRequestMessage();
				
				message.setRequestID(clientIdText.getText());
				//message.setBatteryType("60V20A");
				message.setBatteryType(batteryTypeText.getText());
				//message.setCabinetID("HDG-00001238");
				//message.setCabinetID("EB000001");
				//message.setCabinetID(cabinetIdText.getText());
				message.setEmptyBoxID(cabinetIdText.getText()+"/2");
				message.setFullEnergyBoxID(cabinetIdText.getText()+"/1");
				message.setNotifyTopic(message.getNotifyTopic());
				//newMqttClient.sendSubscribe(message.receiveTopic());
				
				String exchange = ExchangeRequestMessage.encode2Json(message);
				
				newMqttClient.sendPublish(message.destinationTopic(), exchange);*/
				//ServiceMqttClient.getInstance().publish(message.destinationTopic(), exchange);
			}
		});
	}

	public static void main(String[] args) {
		//MqttCfgUtil.loadProps();
		MobileExchangeClient client = new MobileExchangeClient();
	}
}
