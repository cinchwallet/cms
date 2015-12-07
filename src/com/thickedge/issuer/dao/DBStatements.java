package com.thickedge.issuer.dao;

public class DBStatements {

	// create new card holder
	protected String CREATE_NEW_USER = "INSERT INTO CUSTOMER (CUSTOMER_ID, TITLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ADDRESS_LINE1, " +
											"ADDRESS_LINE2, CITY, STATE, COUNTRY, PHONE_WORK, PHONE_HOME, PHONE_MOBILE, EMAIL_ADDRESS, " +
											"IS_ACTIVE, CREATED_TS, UPDATED_TS) " +
											"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	protected String ASSOCIATE_USER_CARD = "INSERT INTO CUSTOMER_CARD (CUSTOMER_ID, CARD_NUMBER, IS_ACTIVE, CREATED_TS, UPDATED_TS) " +
												"VALUES (?,?,?,?,?)";

	protected String DE_ASSOCIATE_USER_CARD = "UPDATE CUSTOMER_CARD SET IS_ACTIVE = false, UPDATED_TS = ? WHERE CUSTOMER_ID = ? AND CARD_NUMBER = ?";

	protected String SELECT_CARD = "select * from CARD where NUMBER = ?";

	protected String UPDTAE_CARD_POINT_BALANCE = "update CARD set points = ?, UPDATED_TIMESTAMP = ? where NUMBER = ?";

	protected String UPDTAE_CARD_STATUS = "update CARD set STATUS = ?, UPDATED_TIMESTAMP = ? where NUMBER = ?";

	protected String SELECT_USER_PROFILE = "SELECT CUST.*, CARD.* FROM CUSTOMER CUST, CUSTOMER_CARD CUSTC, CARD CARD WHERE CUST.CUSTOMER_ID = CUSTC.CUSTOMER_ID AND CARD.NUMBER = CUSTC.CARD_NUMBER AND CUSTC.CARD_NUMBER = ?";

}
