package com.product.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.product_version.model.Version_DAO;
import com.product_version.model.Version_VO;

public class Product_DAO implements Product_DAO_interface {

	
//	 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
		private static DataSource ds = null;
		static {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/petbox");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	
	
//	String driver = "oracle.jdbc.driver.OracleDriver";
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String userid = "PETBOX";
//	String passwd = "123456";
//最新日期
private static final String NewDate = "SELECT P.PRODUCT_ID, P.member_id, P.name, P.product_class, P.product_state, P.create_time, PS.SCORE, PV.PRICE " + 
		"FROM PRODUCT P " + 
		"LEFT JOIN PRODUCT_SCORE PS ON P.PRODUCT_ID =PS.PRODUCT_ID " + 
		"JOIN PRODUCT_VERSION PV ON P.PRODUCT_ID=PV.PRODUCT_ID  " + 
		"ORDER BY P.CREATE_TIME DESC";		
//價錢最高
private static final String HIGH_PRICE="SELECT P.PRODUCT_ID, P.member_id, P.name, P.product_class, P.product_state, P.create_time, PS.SCORE, PV.PRICE " + 
		"FROM PRODUCT P " + 
		"LEFT JOIN PRODUCT_SCORE PS ON P.PRODUCT_ID =PS.PRODUCT_ID " + 
		"JOIN PRODUCT_VERSION PV ON P.PRODUCT_ID=PV.PRODUCT_ID  " + 
		"ORDER BY PV.PRICE DESC";		
//價錢最低
private static final String LOW_PRICE="SELECT P.PRODUCT_ID, P.member_id, P.name, P.product_class, P.product_state, P.create_time, PS.SCORE, PV.PRICE " + 
		"FROM PRODUCT P " + 
		"LEFT JOIN PRODUCT_SCORE PS ON P.PRODUCT_ID =PS.PRODUCT_ID " + 
		"JOIN PRODUCT_VERSION PV ON P.PRODUCT_ID=PV.PRODUCT_ID  " + 
		"ORDER BY PV.PRICE";		
//最高評分
private static final String HIGH_SCORE="SELECT P.PRODUCT_ID, P.member_id, P.name, P.product_class, P.product_state, P.create_time, PS.SCORE, PV.PRICE " + 
		"FROM PRODUCT P " + 
		"LEFT JOIN PRODUCT_SCORE PS ON P.PRODUCT_ID =PS.PRODUCT_ID " + 
		"JOIN PRODUCT_VERSION PV ON P.PRODUCT_ID=PV.PRODUCT_ID  " + 
		"ORDER BY SCORE DESC";		

private static final String All="SELECT P.PRODUCT_ID, P.member_id, P.name, P.product_class, P.product_state, P.create_time, PS.SCORE, PV.PRICE " + 
		"FROM PRODUCT P " + 
		"LEFT JOIN PRODUCT_SCORE PS ON P.PRODUCT_ID =PS.PRODUCT_ID " + 
		"JOIN PRODUCT_VERSION PV ON P.PRODUCT_ID=PV.PRODUCT_ID  " + 
		"ORDER BY P.CREATE_TIME DESC";		

private static final String Getbykeyword="SELECT P.PRODUCT_ID, P.member_id, P.name, P.product_class, P.product_state, P.create_time, PS.SCORE, PV.PRICE " + 
		"FROM PRODUCT P " + 
		"LEFT JOIN PRODUCT_SCORE PS ON P.PRODUCT_ID =PS.PRODUCT_ID " + 
		"JOIN PRODUCT_VERSION PV ON P.PRODUCT_ID=PV.PRODUCT_ID  " + 
		" ORDER BY PV.PRODUCT_VERSION_ID";		

private static final String FinalALL="select * from product ";

private static final String INSERT_STMT = "INSERT INTO product (member_id, name, product_class, description, image1, image2, image3, image4, product_state) VALUES(?,?,?,?,?,?,?,?,?)";
private static final String UPDATE = "UPDATE product SET name=?, product_class=?, description=?, image1=?, image2=?, image3=?, image4=?, product_state=? WHERE product_id=? ";
private static final String DELETE = "DELETE FROM product WHERE product_id=?";
private static final String GET_ONE_STMT = "SELECT product_id, member_id, name, product_class, description, image1, image2, image3, image4, product_state, update_time, create_time FROM PRODUCT WHERE product_id=?";
private static final String GET_BY_MID = "select * from product where member_id = ?";
private static final String GET_BY_CLASS = "select sum(price*quantity) as total, product_class " + 
		"from product_order_detail pod " + 
		"join product_version pv on pv.product_version_id = pod.product_version_id " + 
		"join product p on p.product_id = pv.product_id " + 
		"group by product_class";
	
	@Override
	public Map<String, String> getByClass() {

		try (
				Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(GET_BY_CLASS);
				ResultSet rs = pstmt.executeQuery();
				){
			
			Map<String, String> map = new HashMap<>();

			while (rs.next()) {
				map.put(rs.getString("product_class"), rs.getString("total"));
			}
			return map;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	};
	
	
	
	@Override
	public void insertWithVersion(Product_VO product_VO, List<Version_VO> list) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			
			// 1●設定於 pstm.executeUpdate()之前
    		con.setAutoCommit(false);
    		// 先新增訂單主檔編號
			String cols[] = {"PRODUCT_ID"};
			pstmt = con.prepareStatement(INSERT_STMT , cols);			
			pstmt.setString(1, product_VO.getMember_id());
			pstmt.setString(2, product_VO.getName());
			pstmt.setString(3, product_VO.getProduct_class());
			pstmt.setBytes(4, product_VO.getDescription());
			pstmt.setBytes(5, product_VO.getImage1());
			pstmt.setBytes(6, product_VO.getImage2());
			pstmt.setBytes(7, product_VO.getImage3());
			pstmt.setBytes(8, product_VO.getImage4());
			pstmt.setInt(9, product_VO.getProduct_state());
			pstmt.executeUpdate();
			//抓取對應的自增主鍵值
			String next_product_id = null;
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				next_product_id = rs.getString(1);
				System.out.println("自增主鍵值= " + next_product_id);
			} else {
				System.out.println("未取得自增主鍵值");
			}
			rs.close();
			// 再同時新增明細
			Version_DAO dao = new Version_DAO();
			for (Version_VO aDetail : list) {
				aDetail.setProduct_id(next_product_id) ;
				dao.insert2(aDetail,con);
			}

			// 2●設定於 pstm.executeUpdate()之後
			con.commit();
			con.setAutoCommit(true);
			
			
		} catch (SQLException se) {
			if (con != null) {
				try {
					// 3●設定於當有exception發生時之catch區塊內
					System.err.print("Transaction is being ");
					System.err.println("rolled back-由-dept");
					con.rollback();
				} catch (SQLException excep) {
					throw new RuntimeException("rollback error occured. "
							+ excep.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	
	@Override
	public List<Product_VO> getByMId(String member_id) {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_BY_MID);
			pstmt.setString(1, member_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setDescription(rs.getBytes("description"));
				product_VO.setImage1(rs.getBytes("image1"));
				product_VO.setImage2(rs.getBytes("image2"));
				product_VO.setImage3(rs.getBytes("image3"));
				product_VO.setImage4(rs.getBytes("image4"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				list.add(product_VO);
			}

		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	
	
	
	
	@Override
	public void insert(Product_VO product_VO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1, product_VO.getMember_id());
			pstmt.setString(2, product_VO.getName());
			pstmt.setString(3, product_VO.getProduct_class());
			pstmt.setBytes(4, product_VO.getDescription());
			pstmt.setBytes(5, product_VO.getImage1());
			pstmt.setBytes(6, product_VO.getImage2());
			pstmt.setBytes(7, product_VO.getImage3());
			pstmt.setBytes(8, product_VO.getImage4());
			pstmt.setInt(9, product_VO.getProduct_state());

			pstmt.executeUpdate();

		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void update(Product_VO product_VO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setString(1, product_VO.getName());
			pstmt.setString(2, product_VO.getProduct_class());
			pstmt.setBytes(3, product_VO.getDescription());
			pstmt.setBytes(4, product_VO.getImage1());
			pstmt.setBytes(5, product_VO.getImage2());
			pstmt.setBytes(6, product_VO.getImage3());
			pstmt.setBytes(7, product_VO.getImage4());
			pstmt.setInt(8, product_VO.getProduct_state());
			pstmt.setString(9, product_VO.getProduct_id());
			pstmt.executeUpdate();
			

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured." + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void delete(String Product_id) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, Product_id);

			pstmt.executeUpdate();

			// Handle any driver errors
		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public Product_VO findByPrimaryKey(String Product_id) {
		Product_VO product_VO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, Product_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("Product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));

//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
			}

			// Handle any driver errors
		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return product_VO;
	}
	
	
	
//	以下修改=================================================================================

	@Override
	public List<Product_VO> newDate() {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(NewDate);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				product_VO.setPrice(rs.getInt("price"));
				product_VO.setScore(rs.getDouble("score"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
				list.add(product_VO);
			}

		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	@Override
	public List<Product_VO> getAll(Map<String, String[]> map) {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
			
			con = ds.getConnection();
			String finalSQL = "select * from product "
		          +jdbcUtil_CompositeQuery_Emp2.get_WhereCondition(map)
		          + "order by product_id";
			pstmt = con.prepareStatement(finalSQL);
			System.out.println("●●finalSQL(by DAO) = "+finalSQL);
			rs = pstmt.executeQuery();
System.out.println("rs"+rs);
			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));

				
				list.add(product_VO); // Store the row in the List
				
			}
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	@Override
	public List<Product_VO> highPrice() {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(HIGH_PRICE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				product_VO.setPrice(rs.getInt("price"));
				product_VO.setScore(rs.getDouble("score"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
				list.add(product_VO);
			}

		

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	@Override
	public List<Product_VO> lowPrice() {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(LOW_PRICE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				product_VO.setPrice(rs.getInt("price"));
				product_VO.setScore(rs.getDouble("score"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
				list.add(product_VO);
			}

		

		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	@Override
	public List<Product_VO> highScore() {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			
			con = ds.getConnection();
			pstmt = con.prepareStatement(HIGH_SCORE);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				product_VO.setPrice(rs.getInt("price"));
				product_VO.setScore(rs.getDouble("score"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
				list.add(product_VO);
			}

		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	
	@Override
	public List<Product_VO> all() {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(All);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
				list.add(product_VO);
			}

		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}



	@Override
	public List<Product_VO> getbykeyword() {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(Getbykeyword);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
				list.add(product_VO);
			}

		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}


	@Override
	public List<Product_VO> FinalALL() {
		List<Product_VO> list = new ArrayList<Product_VO>();
		Product_VO product_VO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(FinalALL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				product_VO = new Product_VO();
				product_VO.setProduct_id(rs.getString("product_id"));
				product_VO.setMember_id(rs.getString("member_id"));
				product_VO.setName(rs.getString("name"));
				product_VO.setProduct_class(rs.getString("product_class"));
				product_VO.setProduct_state(rs.getInt("product_state"));
				product_VO.setCreate_time(rs.getDate("create_time"));
				
//				product_VO.setDescription(rs.getBytes("description"));
//				product_VO.setImage1(rs.getBytes("image1"));
//				product_VO.setImage2(rs.getBytes("image2"));
//				product_VO.setImage3(rs.getBytes("image3"));
//				product_VO.setImage4(rs.getBytes("image4"));
				list.add(product_VO);
			}

		
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

}
