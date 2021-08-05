package JDBC1025;

import java.sql.*;
/*
    处理查询结果集（遍历结果集。）
 */
public class JDBCTest05 {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bjpowernode?serverTimezone=UTC&useSSL=false","root","root");
            stmt = conn.createStatement();
            String sql = "select empno as a,ename,sal from emp";
            rs = stmt.executeQuery(sql);//专门执行DQL语句的方法。
            //5、处理查询结果集
            boolean flag1 =  rs.next();
            System.out.println(flag1); //true
            /*if (flag1){
                //光标指向的行有数据
                //取数据
                //getString()方法的特点是：不管数据库中的数据类型是什么，都以String的形式取出。
                String empno = rs.getString(1);//JDBC中所有下标从1开始。不是从0开始。
                String ename = rs.getString(2);
                String sal = rs.getString(3);
                System.out.println(empno+","+ename+","+sal);
            }*/
            while (rs.next()){
                /*String empno = rs.getString(1);
                String ename = rs.getString(2);
                String sal = rs.getString(3);
                System.out.println(empno+","+ename+","+sal);*/

                //这个不是以列的下标获取，以列的名字获取
                //String empno = rs.getString("empno");
                /*String empno = rs.getString("a");//重点注意：列名称不是表中的列名称，是查询结果集的列名称。
                String ename = rs.getString("ename");
                String sal = rs.getString("sal");
                System.out.println(empno+","+ename+","+sal);*/

                //除了可以以String类型取出之外，还可以以特定的类型取出。
                int empno = rs.getInt(1);
                String ename = rs.getString(2);
                double sal = rs.getDouble(3);
                System.out.println(empno+","+ename+","+(sal+100));


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }
}
