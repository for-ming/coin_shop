package sourcecode.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Enumeration;


public class DBConnection {
	
	private static Connection conn;

	private DBConnection() {
	}
	
	static {
		
    // 연결 문자열 생성
    // -접속에 필요한 정보로 구성된 문자열, Connection String
    String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
    String id = "coin";
    String pw = "shop";
    
    // DB작업 > 외부 입출력 > try-catch 필수
    try {
        // JDBC 드라이버 로딩
        Class.forName("oracle.jdbc.driver.OracleDriver");
        
        // DriverManager ( JDBC 드라이버 인스턴스 관리 )
        // getDrivers() : 현재 생성된 JDBC 인스턴스 목록 반환 메소드
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        
        while(driverList.hasMoreElements()) {
        	// Driver 인스턴스를 반환받아서 출력
        	Driver driver = (Driver)driverList.nextElement();
            System.out.println(driver);
        }
        
        // 접속
        // - Connection 객체 생성 + 접속 작업.
        conn = DriverManager.getConnection(url, id, pw);
        System.out.println("conn = "+ conn);
        System.out.println(conn.isClosed()? "접속종료" : "접속중");// 접속중(false), 접속종료(true)
        
    } catch (ClassNotFoundException e) {
        System.out.println("Oracle Driver 클래스 찾을 수 없음" + e.getMessage());
    } catch (SQLException e) {
    	System.out.println("SQL 오류 " + e.getMessage());
    }
   }
	
	
	public static Connection getConnection() {
		return conn;
	}
}
