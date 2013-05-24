import java.sql.*;   
public class GetDataFromMYSQL {   
public static void getdata(){ 

	String driver = "com.mysql.jdbc.Driver";

	// URL指向要访问的数据库名scutcs

	String url = "jdbc:mysql://mysql.973.udms.org/project973_development";

	// MySQL配置时的用户名

	String user = "project973";

	// Java连接MySQL配置时的密码

	String password = "008059c20c2ec4bca8e18ce5c9e2bea0";

	try {

	// 加载驱动程序

	Class.forName(driver);

	// 连续数据库

	Connection conn = DriverManager.getConnection(url, user, password);

	if(!conn.isClosed())

	System.out.println("Succeeded connecting to the Database!");

	// statement用来执行SQL语句

	Statement statement = conn.createStatement();

	// 要执行的SQL语句

	String sql = "select * from student";
    ResultSet rs = statement.executeQuery(sql);  
    System.out.println("-----------------");  
    System.out.println("执行结果如下所示:");  
    System.out.println("-----------------");  
    System.out.println(" 学号" + "\t" + " 姓名");  
    System.out.println("-----------------");  
    String name = null;  
    while(rs.next()) {  
    	name = rs.getString("sname");
    	name = new String(name.getBytes("ISO-8859-1"),"GB2312");
    	System.out.println(rs.getString("sno") + "\t" + name);  
    }  
    rs.close();  
    conn.close();   
    } catch(ClassNotFoundException e) {   
    System.out.println("Sorry,can`t find the Driver!");   
    e.printStackTrace();   
    } catch(SQLException e) {   
    e.printStackTrace();   
    } catch(Exception e) {   
    e.printStackTrace();   
    }
}
}