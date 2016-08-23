package com.lbyl.UI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.lbyl.Biz.ChatServer;
import com.lbyl.Utils.LogUtil;
/**
 * ������
 * ����������¼�
 * �¼�����������رշ�����
 * @author LBYL
 *
 */
public class ServerCtrlUI {

	private ChatServer chatServer;
	private JLabel lb_port;
	private JTextField jtf_openServer;
	private JButton jb_port;
	private JFrame jf;
	private JLabel jl_toast;

	public void initUI() {
		new ServerCtrlUI();
		chatServer = new ChatServer(9090);

		initFrame();// ��ʼ������
		initComp();// ��ʼ�����
		addComp();//add���

		jf.setVisible(true);

	}
	/**
	 * add���
	 */
	private void addComp() {
		jf.add(lb_port);
		jf.add(jtf_openServer);
		jf.add(jb_port);
		jf.add(jl_toast);
	}
	/**
	 * ��ʼ���ؼ�
	 */
	private void initComp() {
		lb_port = new JLabel("�˿ںţ�");
		jtf_openServer = new JTextField(4);
		jtf_openServer.setText("9090");
		jb_port = new JButton("����������");
		jl_toast = new JLabel("");
		
		// �Ӽ�����
		addListeners();
	}

	/**
	 * Ϊ������ϼ�����
	 */
	private void addListeners() {
		jb_port.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				actionServer();
			}

		});
	}
	/**
	 * ��ʼ��Frame
	 */
	private void initFrame() {
		jf = new JFrame("JavaKe");
		jf.setLayout(new FlowLayout());
		jf.setSize(900, 900);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(3);
	}

	/**
	 * ȡ���������֣�����������
	 */
	private void actionServer() {

		String text_port = jtf_openServer.getText();
		if (ChatServer.getRunningState()) {// �����������������
			try {
				chatServer.closeServer();
				jl_toast.setText("�������ѹرա�");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			chatServer.setRunningState(false);// ������ر�״̬
			jb_port.setText("����������");
			return;
		} // ���򴴽�������
		if (text_port != null) {// ��δ������������
			chatServer.setUpServer();
			jl_toast.setText("�����������ɹ���");
			LogUtil.log(ChatServer.getRunningState()+"");
			jb_port.setText("�رշ�����");
		}
	}

}
