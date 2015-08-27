package com.thickedge.issuer.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.thickedge.issuer.constant.DBConstants;

/**
 * using BasicDataSource
 *
 */
public class DBConnection {
    private static Properties             driverInfo       = new Properties();
    private static BasicDataSource        dataSource       = null;
    private static final Logger           logger           = Logger.getLogger(DBConnection.class);
    private static String fileName		= "database.properties";


    public synchronized static void init() throws Exception {
	if (dataSource == null) {
	    InputStream inp = DBConnection.class.getClassLoader().getResourceAsStream(fileName);
	    //InputStream inp  = new FileInputStream(fileName);
	    driverInfo.load(inp);
	     inp.close();
	     setupTxnDataSource();
	}
    }


    private static void setupTxnDataSource() throws Exception {

	dataSource = new BasicDataSource();
	dataSource.setDriverClassName(driverInfo.getProperty(DBConstants.DB_DRIVER_CLASS));
	dataSource.setUsername(driverInfo.getProperty(DBConstants.DB_USER_NAME));
	dataSource.setPassword(driverInfo.getProperty(DBConstants.DB_PASSWORD));
	dataSource.setUrl(driverInfo.getProperty(DBConstants.TXN_DB_URL));
	dataSource.setInitialSize(Integer.parseInt(driverInfo.getProperty(DBConstants.DB_INITIAL_SIZE)));
	dataSource.setMaxWait(Long.parseLong(driverInfo.getProperty(DBConstants.DB_MAX_WAIT)));
	dataSource.setMaxActive(Integer.parseInt(driverInfo.getProperty(DBConstants.DB_MAX_ACTIVE)));
	dataSource.setMaxIdle(Integer.parseInt(driverInfo.getProperty(DBConstants.DB_MAX_IDLE)));
	dataSource.setMinIdle(Integer.parseInt(driverInfo.getProperty(DBConstants.DB_MIN_IDLE)));
	dataSource.setValidationQuery(driverInfo.getProperty(DBConstants.DB_VALIDATION_QUERY));
	dataSource.setTestOnBorrow(Boolean.parseBoolean(driverInfo.getProperty(DBConstants.DB_TESTONBORROW)));
	dataSource.setTestWhileIdle(Boolean.parseBoolean(driverInfo.getProperty(DBConstants.DB_TESTWHILEIDLE)));
	dataSource.addConnectionProperty(DBConstants.DB_REWRITEBATCHSTATEMENTS, "true");
	dataSource.setPoolPreparedStatements(true);
	dataSource.setDefaultTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    }





    public static java.sql.Connection getTxnConnection() throws Exception {
	if (dataSource == null) {
	    init();
	}
	Connection connection = dataSource.getConnection();
	return connection;
    }




    protected static synchronized void reinitializeDatasource() throws Exception {
	logger.warn("DBConnection:: reinitializeDatasource()");
	shutdownDataSource();
	setupTxnDataSource();
    }


    public static void shutdownDataSource() throws SQLException {
	dataSource.close();
    }


    public static void closeAll(ResultSet pResultSet, PreparedStatement pPrepStmt, Connection pCon) {
	if (pResultSet != null) {
	    try {
		pResultSet.close();
	    } catch (Exception _Ex) {
		logger.error("Exception ::", _Ex);
	    }
	}
	if (pPrepStmt != null) {
	    try {
		pPrepStmt.close();
	    } catch (Exception _Ex) {
	    	logger.error("Exception ::", _Ex);
	    }
	}
	if (pCon != null) {
	    try {
		pCon.close();
	    } catch (Exception _Ex) {
	    	logger.error("Exception ::", _Ex);
	    }
	}
    }
}