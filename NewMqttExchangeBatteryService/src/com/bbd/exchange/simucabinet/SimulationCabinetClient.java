package com.bbd.exchange.simucabinet;

import java.awt.Choice;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;

public class SimulationCabinetClient extends JFrame {
	// �õ���ʾ����Ļ�Ŀ���
	static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	// ���崰��Ŀ���
	static int windowsWedth = 600;
	static int windowsHeight = 600;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static NewExchangeMqttClient mqttClient;

	/*
	 * store the request user info.......
	 */
	static JTextField deviceIdText;
	/*
	 * store the cabinetID info.......
	 */
	static JTextField cabinetIdText;
	/*
	 * store the batteryType info.......
	 */
	static JTextField batteryIdText;
	static JTextField boxIdText;
	static Choice boxStatusChoice;
	static Choice batteryExistChoice;
	
	public SimulationCabinetClient() {
		this.setSize(350, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds((width - windowsWedth) / 2, (height - windowsHeight) / 2, windowsWedth, windowsHeight);

		JPanel panel = new JPanel();
		// �������
		add(panel);
		placeComponents(panel);
		setVisible(true);
	}

	private static void placeClientID(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Device ID:");
		passwordLabel.setBounds(10, 100, 150, 25);
		panel.add(passwordLabel);

		deviceIdText = new JTextField(25);
		deviceIdText.setBounds(150, 100, 250, 30);
		deviceIdText.setFont(font);
		deviceIdText.setText("DEVICE-000011");
		panel.add(deviceIdText);
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

	private static void placeBatteryID(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Battery Type:");
		passwordLabel.setBounds(10, 200, 150, 25);
		panel.add(passwordLabel);

		batteryIdText = new JTextField(25);
		batteryIdText.setBounds(150, 200, 250, 30);
		batteryIdText.setFont(font);
		batteryIdText.setText("6AAA121104000111");
		panel.add(batteryIdText);
	}

	private static void placeBoxID(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("Box ID:");
		passwordLabel.setBounds(10, 250, 150, 25);
		panel.add(passwordLabel);

		boxIdText = new JTextField(25);
		boxIdText.setBounds(150, 250, 250, 30);
		boxIdText.setFont(font);
		boxIdText.setText("1");
		panel.add(boxIdText);
	}

	private static void placeBoxStatusChoose(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("door status:");
		passwordLabel.setBounds(10, 300, 100, 25);
		panel.add(passwordLabel);
		
		boxStatusChoice = new Choice();
		boxStatusChoice.add("opened");
		boxStatusChoice.add("closed");
		boxStatusChoice.setBounds(150, 300, 250, 30);
		boxStatusChoice.setSize(200, 120);
		boxStatusChoice.setVisible(true);
		boxStatusChoice.setFont(font);
		panel.add(boxStatusChoice);
	}

	private static void placeBatteryExistChoose(JPanel panel, Font font) {
		JLabel passwordLabel = new JLabel("door status:");
		passwordLabel.setBounds(10, 350, 100, 25);
		panel.add(passwordLabel);
		
		batteryExistChoice = new Choice();
		batteryExistChoice.add("exist");
		batteryExistChoice.add("empty");
		batteryExistChoice.setBounds(150, 350, 250, 30);
		batteryExistChoice.setSize(200, 120);
		batteryExistChoice.setVisible(true);
		batteryExistChoice.setFont(font);
		panel.add(batteryExistChoice);
	}
	
	private static void placeAssoButtonType(JPanel panel, Font font) {
		JButton assoButton = new JButton("Associate");
		assoButton.setBounds(150, 450, 150, 25);
		assoButton.setFont(font);
		panel.add(assoButton);

		assoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UpstreamCabinetMessage msg = new UpstreamCabinetMessage("asso");
				CabinetBoxContainer box = new CabinetBoxContainer("HDG-00001238");

				msg.setDeviceID(deviceIdText.getText());
				msg.setCabinetID(cabinetIdText.getText());
				msg.setVerb("noti");

				box.setId(Integer.parseInt(boxIdText.getText()));
				box.setBatteryExist(true);
				box.setBatteryID(batteryIdText.getText());
				box.setCapacity("90");
				msg.addBox(box);

				if (false == mqttClient.client.isConnected()) {
					mqttClient.connect();
				}

				byte[] buffer = msg.encode();
				if (mqttClient.client.isConnected()) {
					mqttClient.sendPublish(msg.encodeTopic(), buffer);
				}

				// JOptionPane.showMessageDialog(null, "�����Ի���" + cabinetIdText.getText());
			}
		});
	}

	private static void placeNotifyButtonType(JPanel panel, Font font) {
		JButton notifyButton = new JButton("Notify");
		notifyButton.setBounds(150, 500, 150, 25);
		notifyButton.setFont(font);
		panel.add(notifyButton);

		notifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UpstreamCabinetMessage msg = new UpstreamCabinetMessage("asso");
				CabinetBoxContainer box = new CabinetBoxContainer("HDG-00001238");

				msg.setDeviceID(deviceIdText.getText());
				msg.setCabinetID(cabinetIdText.getText());
				msg.setVerb("noti");

				box.setDoorOpened(boxStatusChoice.getSelectedItem().equals("opened") ? true : false);
				box.setId(Integer.parseInt(boxIdText.getText()));
				box.setBatteryExist(batteryExistChoice.getSelectedItem().equals("exist") ? true : false);
				box.setBatteryID(batteryIdText.getText());
				box.setCapacity("90");
				msg.addBox(box);

				if (false == mqttClient.client.isConnected()) {
					mqttClient.connect();
				}

				byte[] buffer = msg.encode();
				if (mqttClient.client.isConnected()) {
					mqttClient.sendPublish(msg.encodeTopic(), buffer);
				}

				// JOptionPane.showMessageDialog(null, "�����Ի���" + cabinetIdText.getText());
			}
		});
	}

	private static void placeComponents(JPanel panel) {

		Font font = new Font(null, Font.TRUETYPE_FONT, 24);

		/*
		 * ���ֲ���������߲��������� ������ò���Ϊ null
		 */
		panel.setLayout(null);

		placeClientID(panel, font);
		placeCabinetID(panel, font);
		placeBatteryID(panel, font);
		placeBoxID(panel, font);

		placeAssoButtonType(panel, font);
		placeNotifyButtonType(panel, font);
		// create exchange button
		placeBoxStatusChoose(panel, font);
		placeBatteryExistChoose(panel, font);
	}

	public static void main(String[] args) {
		SimulationCabinetClient client = new SimulationCabinetClient();
		mqttClient = new NewExchangeMqttClient("tcp://121.40.109.91", "parry", "parry123", "DEV-3245662ER",
				new SimulationCabinetMqttMsgCallback());

		mqttClient.connect();
	}
}