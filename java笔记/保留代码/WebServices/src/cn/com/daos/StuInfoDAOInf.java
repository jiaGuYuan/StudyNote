package cn.com.daos;

import java.util.List;

import cn.com.beans.StuInfoBean;

public interface StuInfoDAOInf {
	List<StuInfoBean> getAllStuInfo(String address);
	boolean addStuInfo(StuInfoBean s);
}
