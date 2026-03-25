package com.raissapayments.conector.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class DynamicDataSourceConfig {
    @Value("${connect.api.server}")
    private String urlServer;

    @Value("${connect.api.username}")
    private String userBd;

    @Value("${connect.api.password}")
    private String passwordBd;

    @Value("${connect.api.driver-class-name}")
    private String driverBd;

    @Bean
    public DataSource dataSource() {
        String empresa1Url = "jdbc:postgresql://" + urlServer + "/bd1";
        String empresa2Url = "jdbc:postgresql://" + urlServer + "/bd2";
        String empresa3Url = "jdbc:postgresql://" + urlServer + "/bd3";
        String empresa4Url = "jdbc:postgresql://" + urlServer + "/bd4";
        String empresa5Url = "jdbc:postgresql://" + urlServer + "/bd5";
        String empresa6Url = "jdbc:postgresql://" + urlServer + "/bd6";
        String empresa7Url = "jdbc:postgresql://" + urlServer + "/bd7";
        String empresa8Url = "jdbc:postgresql://" + urlServer + "/bd8";
        String empresa9Url = "jdbc:postgresql://" + urlServer + "/bd9";
        String empresa10Url = "jdbc:postgresql://" + urlServer + "/bd10";


        DataSource dataSourceEmpresa1 = createDataSource(empresa1Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa2 = createDataSource(empresa2Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa3 = createDataSource(empresa3Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa4 = createDataSource(empresa4Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa5 = createDataSource(empresa5Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa6 = createDataSource(empresa6Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa7 = createDataSource(empresa7Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa8 = createDataSource(empresa8Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa9 = createDataSource(empresa9Url, userBd, passwordBd, driverBd);
        DataSource dataSourceEmpresa10 = createDataSource(empresa10Url, userBd, passwordBd, driverBd);

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("bd1", dataSourceEmpresa1);
        targetDataSources.put("bd2", dataSourceEmpresa2);
        targetDataSources.put("bd3", dataSourceEmpresa3);
        targetDataSources.put("bd4", dataSourceEmpresa4);
        targetDataSources.put("bd5", dataSourceEmpresa5);
        targetDataSources.put("bd6", dataSourceEmpresa6);
        targetDataSources.put("bd7", dataSourceEmpresa7);
        targetDataSources.put("bd8", dataSourceEmpresa8);
        targetDataSources.put("bd9", dataSourceEmpresa9);
        targetDataSources.put("bd10", dataSourceEmpresa10);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(dataSourceEmpresa1);

        String empresaSeleccionada = getEmpresaSeleccionada();
        if (targetDataSources.containsKey(empresaSeleccionada)) {
            dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get(empresaSeleccionada));  // Cambiar la fuente de datos predeterminada
        }

        return dynamicDataSource;
    }

    private DataSource createDataSource(String url, String username, String password, String driverClassName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    private String getEmpresaSeleccionada() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            String empresaSeleccionada = attributes.getRequest().getHeader("idDsEmpresa");
            return empresaSeleccionada != null ? empresaSeleccionada : "bd1";  // Si no está presente, por defecto retorna "empresa1"
        }

        return "bd1";
    }
}
