package JDBC1025;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/*
    JDBC完成delete update
 */
public class JDBCTest02 {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode?serverTimezone=UTC&useSSL=false","root","root");
            stmt = conn.createStatement();
            //String sql = "delete from dept where deptno = 40";
            //JDBC中的sql语句不需要提供分号结尾。
            String sql = "update dept set dname = '销售部', loc = '天津' where deptno = 20";
            int count = stmt.executeUpdate(sql);
            System.out.println(count==1?"删除成功":"删除失败");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(stmt !=null){
                try {
                    stmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
