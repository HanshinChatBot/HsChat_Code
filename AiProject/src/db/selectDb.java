package db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;


public class selectDb {
	public static ArrayList<String> select() {
		  Connection conn = null;
	      Statement stmt = null;
	      ResultSet rs;
	      ArrayList<String> result = new ArrayList<String>();
	      try {
	    	  Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC driver를 메모리에 로드
		      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/AI?serverTimezone=UTC", "root", "123456");
		      stmt =  conn.createStatement();
		      rs = stmt.executeQuery("SELECT * FROM Homepage"); // 원하는 쿼리문 실행
		      while (rs.next()) {
		         System.out.println(rs.getString("word"));
		         result.add(rs.getString("word"));
		      }
		      for(String a: result) {
		    	  System.out.println(a+" 씨발");
		      }
		      rs.close();
		      stmt.close();
		      conn.close();
	        } catch (ClassNotFoundException e) {
	            System.out.println("드라이버 로딩 실패");
	        } catch (SQLException e) {
	            System.out.println("에러 " + e);
	        } finally {
	            try {
	                if (conn != null && !conn.isClosed()) {
	                    conn.close();
	                }
	                if (stmt != null && !stmt.isClosed()) {
	                    stmt.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
		return result;
	      
	   }
	}




