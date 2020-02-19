package com.thekuzea.experimental.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static void loadSqlAndExecute(final DataSource dataSource,
                                         final String path) throws SQLException {

        final Resource resource = new ClassPathResource(path);
        ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
    }
}
