package cn.com.beans;

import com.service.ActionForm;

public class UserBean extends ActionForm {
	private String txtUserName;
	private String pwdUserPwd;
	private String rabSex;
	private String cmbCity;

	public String getTxtUserName() {
		return txtUserName;
	}
	public void setTxtUserName(String txtUserName) {
		this.txtUserName = txtUserName;
	}
	public String getPwdUserPwd() {
		return pwdUserPwd;
	}
	public void setPwdUserPwd(String pwdUserPwd) {
		this.pwdUserPwd = pwdUserPwd;
	}
	public String getRabSex() {
		return rabSex;
	}
	public void setRabSex(String rabSex) {
		this.rabSex = rabSex;
	}
	public String getCmbCity() {
		return cmbCity;
	}
	public void setCmbCity(String cmbCity) {
		this.cmbCity = cmbCity;
	}
	
}
