package cn.com.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.com.beans.StuInfoBean;
import cn.com.db.DBUtil;

public class StuInfoDAOImpl implements StuInfoDAOInf {
	@Override
	public List<StuInfoBean> getAllStuInfo(String address) {
		// TODO Auto-generated method stub
		List<StuInfoBean> list = new ArrayList<StuInfoBean>();
		Connection conn = DBUtil.getConn();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			
			String sql = "select * from stuInfo where stu_address like ?";
			//获得操作句柄
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, "%"+address+"%");
			rs = pstm.executeQuery();
			StuInfoBean s = null;
			//操作结果集
			while(rs.next()) {
				s = new StuInfoBean();
				s.setStuId(rs.getInt("stu_id"));
				s.setStuNumber(rs.getString("stu_number"));
				s.setStuName(rs.getString("stu_name"));
				s.setStuAge(rs.getInt("stu_age"));
				s.setStuSex(rs.getString("stu_sex"));
				s.setStuCard(rs.getString("stu_card"));
				s.setStuJoinTime(rs.getString("stu_joinTime"));
				s.setStuAddress(rs.getString("stu_address"));
				list.add(s);
			}
			
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.free(rs,pstm,conn);
		}
		return list;
	}

	@Override
	public boolean addStuInfo(StuInfoBean s) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConn();
		PreparedStatement pstm = null;
		boolean bool = false;
		try {
			
			//获得连接
			//? 占位符 
			String sql = "insert into stuInfo values(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd'),?)";
			
			//获得操作句柄
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1,s.getStuId());
			pstm.setString(2,s.getStuNumber());
			pstm.setString(3,s.getStuName());
			pstm.setInt(4,s.getStuAge());
			pstm.setString(5,s.getStuSex());
			pstm.setString(6,s.getStuCard());
			pstm.setString(7,s.getStuJoinTime());
			pstm.setString(8,s.getStuAddress());
			
			int len = pstm.executeUpdate();
			if(len > 0) {
				bool = true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			DBUtil.free(pstm,conn);
		}
		return bool;
	}

}
