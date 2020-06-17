package com.example.multipledatasource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.sql.DataSourceDefinition;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MultipleDatasourceApplicationTests {

    @Autowired
    DataSource dataSource2;

    @Autowired
    @Qualifier("DataSource3")
    DataSource dataSource3;

    @Test
    void contextLoads() throws SQLException {
        String sql = "SELECT USERNAME FROM USER WHERE UID='111'";
        Connection connection1 = dataSource2.getConnection();
        Connection connection2 = dataSource3.getConnection();
        PreparedStatement preparedStatement1 = connection1.prepareStatement(sql);
        PreparedStatement preparedStatement2 = connection2.prepareStatement(sql);
        ResultSet result1 =  preparedStatement1.executeQuery();
        ResultSet result2 =  preparedStatement2.executeQuery();
        result1.next();
        result2.next();
        System.out.println(dataSource2.getClass()+":"+result1.getString(1));
        System.out.println(dataSource3.getClass()+":"+result2.getString(1));
        connection1.close();
        connection2.close();
    }

}
