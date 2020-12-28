package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class connectDb {
	
    public static void databaseInsert(String text) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/AI?serverTimezone=UTC", "root", "123456");
            pstmt = conn.prepareStatement("INSERT INTO Homepage VALUES (null, ?)");
            pstmt.setString(1, text);

            int count = pstmt.executeUpdate();
            System.out.println(count+" 카운트 수");
            if (count == 0) {
                System.out.println("데이터 입력 실패");
            } else {
                System.out.println("데이터 입력 성공");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패");
        } catch (SQLException e) {
            System.out.println("에러 " + e);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } 
}