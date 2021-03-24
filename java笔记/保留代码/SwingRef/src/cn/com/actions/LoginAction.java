package cn.com.actions;

import java.awt.AWTEvent;

import cn.com.beans.UserBean;
import cn.com.views.LoginFrame;

import com.service.Action;
import com.service.ActionForm;

public class LoginAction extends Action {

	@Override
	public void execute(ActionForm form, Object o, AWTEvent e) {
		// TODO Auto-generated method stub
		UserBean u = (UserBean)form;
		LoginFrame lf = (LoginFrame)o;
		
		System.out.println(u.getTxtUserName());
		System.out.println(u.getPwdUserPwd());
		System.out.println(u.getRabSex());
		System.out.println(u.getCmbCity());
	}

}
