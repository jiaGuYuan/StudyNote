package com.service;

import java.awt.AWTEvent;

public abstract class Action {
	public abstract void execute(ActionForm form,Object o,AWTEvent e);
}
