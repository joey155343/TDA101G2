package com.store.model;

import static com.store.common.Common.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.store_closed.model.Store_closedDAO;
import com.store_closed.model.Store_closedVO;

public class StoreDAO implements StoreDAO_interface {
	
	private static DataSource datasource = null;
	static {
		try {
			Context context = new InitialContext();
			datasource = (DataSource) context.lookup("java:comp/env/jdbc/petbox");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	// insert into 
	public static final String INSERT = "insert into store(store_id, member_id, store_class, store_name, store_adress, store_phone_number,"
			+ " store_introduction, store_clicks, store_firstbreak, store_secondbreak, store_openhours1,"
			+ " store_openhours2 ,store_openhours3 ,store_timelimit, store_maxcapacity, store_image1,"
			+ " store_image2, store_image3,store_on, update_time)"
			+ " values ('S'||LPAD(to_char(seq_store_id.nextval),5,'0'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	public static final String UPDATE = "update store set member_id=?, store_class=?, store_name=?, store_adress=?, store_phone_number=?,"
			+ " store_introduction=?, store_clicks=?, store_firstbreak=?, store_secondbreak=?, store_openhours1=?,"
			+ " store_openhours2=? ,store_openhours3=? ,store_timelimit=?, store_maxcapacity=?,"
			+ " store_image1=?, store_image2=?, store_image3=? ,store_on=?, update_time=? where store_id= ? ";
	public static final String DELETE = "delete from store where store_id= ? ";
	public static final String SELECT_BY_STORE_ID = "select * from store where store_id= ? ";
	public static final String SELECT_BY_MEMBER_ID = "select * from store where member_id= ? ";
	public static final String SELECT_BY_CLASS = "select * from store where store_class= ? ";
	public static final String SELECT_BY_CLASS2 = "select store_id, member_id, store_class, store_name, store_adress, store_phone_number,\r\n" + 
			"store_introduction, store_clicks, store_firstbreak, store_secondbreak, store_image1,\r\n" + 
			"store_on from store where store_class= ? ";
	public static final String SELECT_ALL = "select * from store ";

	@Override
	public void insert(StoreVO storeVO) {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(INSERT);

			ps.setString(1, storeVO.getMember_id());
			ps.setString(2, storeVO.getStore_class());
			ps.setString(3, storeVO.getStore_name());
			ps.setString(4, storeVO.getStore_adress());
			ps.setString(5, storeVO.getStore_phone_number());
			ps.setString(6, storeVO.getStore_introduction());
			ps.setObject(7, storeVO.getStore_clicks(), java.sql.Types.INTEGER);
			ps.setObject(8, storeVO.getStore_firstbreak(), java.sql.Types.INTEGER);
			ps.setObject(9, storeVO.getStore_secondbreak(), java.sql.Types.INTEGER);
			ps.setString(10, storeVO.getStore_openhours1());
			ps.setString(11, storeVO.getStore_openhours2());
			ps.setString(12, storeVO.getStore_openhours3());
			ps.setObject(13, storeVO.getStore_timelimit(), java.sql.Types.INTEGER);
			ps.setObject(14, storeVO.getStore_maxcapacity(), java.sql.Types.INTEGER);
			ps.setBytes(15, storeVO.getStore_image1());
			ps.setBytes(16, storeVO.getStore_image2());
			ps.setBytes(17, storeVO.getStore_image3());
			ps.setObject(18, storeVO.getStore_on(), java.sql.Types.INTEGER);
			ps.setTimestamp(19, storeVO.getUpdate_time());
			ps.executeUpdate();

//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public StoreVO update(StoreVO storeVO) {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(UPDATE);

			ps.setString(1, storeVO.getMember_id());
			ps.setString(2, storeVO.getStore_class());
			ps.setString(3, storeVO.getStore_name());
			ps.setString(4, storeVO.getStore_adress());
			ps.setString(5, storeVO.getStore_phone_number());
			ps.setString(6, storeVO.getStore_introduction());
			ps.setObject(7, storeVO.getStore_clicks(), java.sql.Types.INTEGER);
			ps.setObject(8, storeVO.getStore_firstbreak(), java.sql.Types.INTEGER);
			ps.setObject(9, storeVO.getStore_secondbreak(), java.sql.Types.INTEGER);
			ps.setString(10, storeVO.getStore_openhours1());
			ps.setString(11, storeVO.getStore_openhours2());
			ps.setString(12, storeVO.getStore_openhours3());
			ps.setObject(13, storeVO.getStore_timelimit(), java.sql.Types.INTEGER);
			ps.setObject(14, storeVO.getStore_maxcapacity(), java.sql.Types.INTEGER);
			ps.setBytes(15, storeVO.getStore_image1());
			ps.setBytes(16, storeVO.getStore_image2());
			ps.setBytes(17, storeVO.getStore_image3());
			ps.setObject(18, storeVO.getStore_on(), java.sql.Types.INTEGER);
			ps.setTimestamp(19, storeVO.getUpdate_time());
			ps.setString(20, storeVO.getStore_id());

			ps.executeUpdate();

			return storeVO;
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void delete(String store_id) {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(DELETE);

			ps.setString(1, store_id);

			ps.executeUpdate();

//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public StoreVO findByPK(String store_id) {
		StoreVO storeVO = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(SELECT_BY_STORE_ID);
			ps.setString(1, store_id);
			rs = ps.executeQuery();

			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setMember_id(rs.getString("member_id"));
				storeVO.setStore_class(rs.getString("store_class"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_adress(rs.getString("store_adress"));
				storeVO.setStore_phone_number(rs.getString("store_phone_number"));
				storeVO.setStore_introduction(rs.getString("store_introduction"));
				storeVO.setStore_clicks(rs.getInt("store_clicks"));
				storeVO.setStore_firstbreak(rs.getInt("store_firstbreak"));
				storeVO.setStore_secondbreak(rs.getInt("store_secondbreak"));
				storeVO.setStore_openhours1(rs.getString("store_openhours1"));
				storeVO.setStore_openhours2(rs.getString("store_openhours2"));
				storeVO.setStore_openhours3(rs.getString("store_openhours3"));
				storeVO.setStore_timelimit(rs.getInt("store_timelimit"));
				storeVO.setStore_maxcapacity(rs.getInt("store_maxcapacity"));
				storeVO.setStore_image1(rs.getBytes("store_image1"));
				storeVO.setStore_image2(rs.getBytes("store_image2"));
				storeVO.setStore_image3(rs.getBytes("store_image3"));
				storeVO.setStore_on(rs.getInt("store_on"));
				storeVO.setCreate_time(rs.getTimestamp("create_time"));
				storeVO.setUpdate_time(rs.getTimestamp("update_time"));
			}

//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
			
		}

		return storeVO;
	}
	@Override
	public StoreVO findByMemberId(String member_id) {
		StoreVO storeVO = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(SELECT_BY_MEMBER_ID);
			ps.setString(1, member_id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setMember_id(rs.getString("member_id"));
				storeVO.setStore_class(rs.getString("store_class"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_adress(rs.getString("store_adress"));
				storeVO.setStore_phone_number(rs.getString("store_phone_number"));
				storeVO.setStore_introduction(rs.getString("store_introduction"));
				storeVO.setStore_clicks(rs.getInt("store_clicks"));
				storeVO.setStore_firstbreak(rs.getInt("store_firstbreak"));
				storeVO.setStore_secondbreak(rs.getInt("store_secondbreak"));
				storeVO.setStore_openhours1(rs.getString("store_openhours1"));
				storeVO.setStore_openhours2(rs.getString("store_openhours2"));
				storeVO.setStore_openhours3(rs.getString("store_openhours3"));
				storeVO.setStore_timelimit(rs.getInt("store_timelimit"));
				storeVO.setStore_maxcapacity(rs.getInt("store_maxcapacity"));
				storeVO.setStore_image1(rs.getBytes("store_image1"));
				storeVO.setStore_image2(rs.getBytes("store_image2"));
				storeVO.setStore_image3(rs.getBytes("store_image3"));
				storeVO.setStore_on(rs.getInt("store_on"));
				storeVO.setCreate_time(rs.getTimestamp("create_time"));
				storeVO.setUpdate_time(rs.getTimestamp("update_time"));
			}
			
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
			
		}
		
		return storeVO;
	}

	@Override
	public List<StoreVO> findByClass(String store_class) {
		List<StoreVO> list = new ArrayList<StoreVO>();
		StoreVO storeVO = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(SELECT_BY_CLASS);
			ps.setString(1, store_class);
			rs = ps.executeQuery();
			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setMember_id(rs.getString("member_id"));
				storeVO.setStore_class(rs.getString("store_class"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_adress(rs.getString("store_adress"));
				storeVO.setStore_phone_number(rs.getString("store_phone_number"));
				storeVO.setStore_introduction(rs.getString("store_introduction"));
				storeVO.setStore_clicks(rs.getInt("store_clicks"));
				storeVO.setStore_firstbreak(rs.getInt("store_firstbreak"));
				storeVO.setStore_secondbreak(rs.getInt("store_secondbreak"));
				storeVO.setStore_openhours1(rs.getString("store_openhours1"));
				storeVO.setStore_openhours2(rs.getString("store_openhours2"));
				storeVO.setStore_openhours3(rs.getString("store_openhours3"));
				storeVO.setStore_timelimit(rs.getInt("store_timelimit"));
				storeVO.setStore_maxcapacity(rs.getInt("store_maxcapacity"));
				storeVO.setStore_image1(rs.getBytes("store_image1"));
				storeVO.setStore_image2(rs.getBytes("store_image2"));
				storeVO.setStore_image3(rs.getBytes("store_image3"));
				storeVO.setStore_on(rs.getInt("store_on"));
				storeVO.setCreate_time(rs.getTimestamp("create_time"));
				storeVO.setUpdate_time(rs.getTimestamp("update_time"));
				list.add(storeVO);

			}

//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

		return list;
	}
	@Override
	public List<StoreVO> findByClass2(String store_class) {
		List<StoreVO> list = new ArrayList<StoreVO>();
		StoreVO storeVO = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(SELECT_BY_CLASS2);
			ps.setString(1, store_class);
			rs = ps.executeQuery();
			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setMember_id(rs.getString("member_id"));
				storeVO.setStore_class(rs.getString("store_class"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_adress(rs.getString("store_adress"));
				storeVO.setStore_phone_number(rs.getString("store_phone_number"));
				storeVO.setStore_introduction(rs.getString("store_introduction"));
				storeVO.setStore_clicks(rs.getInt("store_clicks"));
				storeVO.setStore_firstbreak(rs.getInt("store_firstbreak"));
				storeVO.setStore_secondbreak(rs.getInt("store_secondbreak"));
				storeVO.setStore_image1(rs.getBytes("store_image1"));
				storeVO.setStore_on(rs.getInt("store_on"));
				list.add(storeVO);
				
			}
			
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
		return list;
	}

	@Override
	public List<StoreVO> getAll() {
		List<StoreVO> list = new ArrayList<StoreVO>();
		StoreVO storeVO = null;

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			ps = conn.prepareStatement(SELECT_ALL);
			rs = ps.executeQuery();

			while (rs.next()) {
				storeVO = new StoreVO();
				storeVO.setStore_id(rs.getString("store_id"));
				storeVO.setMember_id(rs.getString("member_id"));
				storeVO.setStore_class(rs.getString("store_class"));
				storeVO.setStore_name(rs.getString("store_name"));
				storeVO.setStore_adress(rs.getString("store_adress"));
				storeVO.setStore_phone_number(rs.getString("store_phone_number"));
				storeVO.setStore_introduction(rs.getString("store_introduction"));
				storeVO.setStore_clicks(rs.getInt("store_clicks"));
				storeVO.setStore_firstbreak(rs.getInt("store_firstbreak"));
				storeVO.setStore_secondbreak(rs.getInt("store_secondbreak"));
				storeVO.setStore_openhours1(rs.getString("store_openhours1"));
				storeVO.setStore_openhours2(rs.getString("store_openhours2"));
				storeVO.setStore_openhours3(rs.getString("store_openhours3"));
				storeVO.setStore_timelimit(rs.getInt("store_timelimit"));
				storeVO.setStore_maxcapacity(rs.getInt("store_maxcapacity"));
				storeVO.setStore_image1(rs.getBytes("store_image1"));
				storeVO.setStore_image2(rs.getBytes("store_image2"));
				storeVO.setStore_image3(rs.getBytes("store_image3"));
				storeVO.setStore_on(rs.getInt("store_on"));
				storeVO.setCreate_time(rs.getTimestamp("create_time"));
				storeVO.setUpdate_time(rs.getTimestamp("update_time"));
				list.add(storeVO);
			}

//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
//	@Override
	public void insertWithClosed(StoreVO storeVO, List<Store_closedVO> list) {

		Connection conn = null;
		PreparedStatement ps = null;

		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, userId, passWord);
			conn = datasource.getConnection();
			
			conn.setAutoCommit(false);
			
			String next_storeId;
			ResultSet rs;
			String cols[] = {"STORE_ID"};
			ps = conn.prepareStatement(INSERT, cols);

			ps.setString(1, storeVO.getMember_id());
			ps.setString(2, storeVO.getStore_class());
			ps.setString(3, storeVO.getStore_name());
			ps.setString(4, storeVO.getStore_adress());
			ps.setString(5, storeVO.getStore_phone_number());
			ps.setString(6, storeVO.getStore_introduction());
			ps.setObject(7, storeVO.getStore_clicks(), java.sql.Types.INTEGER);
			ps.setObject(8, storeVO.getStore_firstbreak(), java.sql.Types.INTEGER);
			ps.setObject(9, storeVO.getStore_secondbreak(), java.sql.Types.INTEGER);
			ps.setString(10, storeVO.getStore_openhours1());
			ps.setString(11, storeVO.getStore_openhours2());
			ps.setString(12, storeVO.getStore_openhours3());
			ps.setObject(13, storeVO.getStore_timelimit(), java.sql.Types.INTEGER);
			ps.setObject(14, storeVO.getStore_maxcapacity(), java.sql.Types.INTEGER);
			ps.setBytes(15, storeVO.getStore_image1());
			ps.setBytes(16, storeVO.getStore_image2());
			ps.setBytes(17, storeVO.getStore_image3());
			ps.setObject(18, storeVO.getStore_on(), java.sql.Types.INTEGER);
			ps.setTimestamp(19, storeVO.getUpdate_time());
			ps.executeUpdate();
			
			next_storeId = null;
			rs = ps.getGeneratedKeys();
			if(rs.next()) {
				next_storeId = rs.getString(1);
				System.out.println("自增主鍵值= " + next_storeId +"(剛新增成功的店家編號)");
			} else {
				System.out.println("未取得自增主鍵值");
			}
			rs.close();
			
			Store_closedDAO dao = new Store_closedDAO();
			System.out.println("list.size()-A="+list.size());
			for (Store_closedVO store_closedVO : list) {
				store_closedVO.setStore_id(next_storeId);
				dao.insert2(store_closedVO,conn);
			}
			
			conn.commit();
			conn.setAutoCommit(true);
			System.out.println("新增店家" + next_storeId + "時,共有" + list.size()
					+ "公休日被新增");
			
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException("Couldn't load database driver： " + e.getMessage());
		} catch (SQLException e) {
			try {
				System.err.print("Transaction is being ");
				System.err.println("rolled back-由-Store");
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException("A database error occured： " + e.getMessage());
		} finally {
//			try {
//				
//			} catch (SQLException e1) {
//				throw new RuntimeException("rollback error occured. "
//						+ e1.getMessage());
//			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

}
