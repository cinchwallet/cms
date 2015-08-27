package com.thickedge.issuer.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;
import com.thickedge.issuer.service.TransactionService;

public class TransactionProcessor {

    private static TransactionProcessor transactionProcessor = null;

    private static Properties           responseCodeMap      = null;

    private TransactionProcessor() {}

    public static TransactionProcessor getInstance() {
	if (transactionProcessor == null) {
	    transactionProcessor = new TransactionProcessor();
	}
	return transactionProcessor;
    }

    public Response getCustomerDetail(Request request) {
	// Get the card balance.
	Response response = TransactionService.getInstance().getCustomerDetail(request);

	return response;
    }

    public Response getCardBalance(Request request) {
	// Get the card balance.
	Response response = TransactionService.getInstance().getBalance(request);

	return response;
    }

    public Response doSale(Request request) {
	Response response = TransactionService.getInstance().doSale(request);
	return response;
    }

    public Response doReload(Request request) {
	Response response = TransactionService.getInstance().doReload(request);
	return response;
    }

    public Response activateCard(Request request) {
	Response response = TransactionService.getInstance().activateCard(request);
	return response;
    }

    public Response deactivateCard(Request request) {
	Response response = TransactionService.getInstance().deactivateCard(request);
	return response;
    }

    private void populateResponseMsg(Response response) {
	if (responseCodeMap == null) {
	    // load the response Code map
	    try {
		InputStream inputStream = TransactionProcessor.class.getClassLoader().getResourceAsStream("response_code.properties");
		responseCodeMap.load(inputStream);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	response.setResponseMsg(response.getResponseCode());
    }
}
