package cn.com.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import cn.com.listeners.LoginFrame_btnLogin_ActionListener;

public class LoginFrame extends JFrame {
	private JPanel pnlMain;
	private JLabel lblUserNo;
	private JLabel lblUserPwd;
	private JTextField txtUserName;
	private JPasswordField pwdUserPwd;
	private JRadioButton rabSexM;
	private JRadioButton rabSexF;
	private JComboBox cmbCity;
	private JButton btnLogin;
	private ButtonGroup btgSex;
	
	public LoginFrame() {
		pnlMain = new JPanel();
		lblUserNo = new JLabel("’À∫≈£∫");
		lblUserPwd = new JLabel("√‹¬Î£∫");
		txtUserName = new JTextField(10);
		pwdUserPwd = new JPasswordField(10);
		rabSexM = new JRadioButton("ƒ–");
		rabSexF = new JRadioButton("≈Æ");
		cmbCity = new JComboBox();
		btgSex = new ButtonGroup();
		btnLogin = new JButton("µ«¬º");
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 400);
		
		btnLogin.addActionListener(new LoginFrame_btnLogin_ActionListener(this));
		
		btgSex.add(rabSexM);
		btgSex.add(rabSexF);
		cmbCity.addItem("≥§…≥");
		cmbCity.addItem("÷Í÷ﬁ");
		cmbCity.addItem("œÊÃ∂");
		cmbCity.addItem("”¿÷›");
		
		pnlMain.add(lblUserNo);
		pnlMain.add(txtUserName);
		pnlMain.add(lblUserPwd);
		pnlMain.add(pwdUserPwd);
		pnlMain.add(rabSexM);
		pnlMain.add(rabSexF);
		pnlMain.add(cmbCity);
		pnlMain.add(btnLogin);
		this.add(pnlMain);
		this.setVisible(true);
	}
}
