package com.example.canary.common.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhaohongliang 2023-10-20 02:47
 * @since 1.0
 */
@Slf4j
@Component
public class DataSourceHealthIndicator  implements HealthIndicator {

    @Autowired
    @Qualifier("masterDataSource")
    private DataSource masterDataSource;

    @Autowired
    @Qualifier("slave1DataSource")
    private DataSource slave1DataSource;

    @Override
    public Health health() {
        // 检查主库和备库的健康状态
        boolean isMasterHealthy = checkMasterHealth();
        boolean isSlaveHealthy = checkSlaveHealth();

        if (isMasterHealthy && isSlaveHealthy) {
            return Health.up().build();
        } else if (isMasterHealthy) {
            return Health.up().withDetail(DataSourceEnum.SLAVE1.name(), "not healthy").build();
        } else if (isSlaveHealthy) {
            return Health.up().withDetail(DataSourceEnum.MASTER.name(), "not healthy").build();
        } else {
            return Health.down().build();
        }
    }

    /**
     * // 检查主库的健康状态
     *
     * @return
     */
    private boolean checkMasterHealth() {
        Connection masterConn = null;
        try {
            masterConn = masterDataSource.getConnection();
            masterConn.close();
            if (masterConn.isValid(5)) {
                return true;
            }
        } catch (SQLException e) {
            log.error("数据源主库写入服务发生异常，异常信息：" + e.getMessage());
        } finally {
            if (masterConn != null) {
                try {
                    masterConn.close();
                } catch (SQLException e) {
                    log.error("关闭主库连接发生异常，异常信息：" + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * 检查备库的健康状态
     *
     * @return
     */
    private boolean checkSlaveHealth() {
        Connection slave1Conn = null;
        try {
            slave1Conn = slave1DataSource.getConnection();
            if (slave1Conn.isValid(5)) {
                return true;
            }
        } catch (SQLException e) {
            log.error("数据源备库写入服务发生异常，异常信息：" + e.getMessage());
        } finally {
            if (slave1Conn != null) {
                try {
                    slave1Conn.close();
                } catch (SQLException e) {
                    log.error("关闭备库连接发生异常，异常信息：" + e.getMessage());
                }
            }
        }
        return false;
    }
}
