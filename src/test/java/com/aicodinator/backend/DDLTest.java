package com.aicodinator.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;

@SpringBootTest
@ActiveProfiles("local")
public class DDLTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void printSubsidiesTableStructure() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            System.out.println("\n=== SUBSIDIES 테이블 구조 ===");
            ResultSet columns = metaData.getColumns(null, null, "SUBSIDIES", null);
            
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String nullable = columns.getString("IS_NULLABLE");
                
                System.out.printf("컬럼명: %-25s 타입: %-15s 크기: %-10d NULL: %s%n", 
                    columnName, dataType, columnSize, nullable);
            }
        }
    }
    
    @Test
    public void printYouthPoliciesTableStructure() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            System.out.println("\n=== YOUTH_POLICIES 테이블 구조 ===");
            ResultSet columns = metaData.getColumns(null, null, "YOUTH_POLICIES", null);
            
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String nullable = columns.getString("IS_NULLABLE");
                
                System.out.printf("컬럼명: %-25s 타입: %-15s 크기: %-10d NULL: %s%n", 
                    columnName, dataType, columnSize, nullable);
            }
        }
    }
}