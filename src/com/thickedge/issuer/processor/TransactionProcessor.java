package com.thickedge.issuer.processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;

import com.thickedge.issuer.constant.AppConstant;
import com.thickedge.issuer.constant.AppConstant.OperationType;
import com.thickedge.issuer.constant.ResponseCode;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;
import com.thickedge.issuer.service.TransactionService;

public class TransactionProcessor {

	private static TransactionProcessor transactionProcessor = null;

	private static Properties responseCodeMap = null;

	private TransactionProcessor() {
	}

	public static TransactionProcessor getInstance() {
		if (transactionProcessor == null) {
			transactionProcessor = new TransactionProcessor();
		}
		return transactionProcessor;
	}

	public Response processTransaction(Request request,
			OperationType operationType) {
		ObjectMapper mapper = new ObjectMapper();
		Response response;
		try {
			System.out.println("Request received for :"+ operationType.getType() + " : "+mapper.writeValueAsString(request));
			response = null;
			if (operationType.equals(AppConstant.OperationType.REGISTERUSER)) {
				response = TransactionService.getInstance().registerUser(request);
			} else if (operationType.equals(AppConstant.OperationType.CARDDETAIL)) {
				response = TransactionService.getInstance().getCardDetail(request);
			} else if (operationType.equals(AppConstant.OperationType.USEPROFILE_C)) {
				response = TransactionService.getInstance().getUserProfileByCard(request);
			} else if (operationType.equals(AppConstant.OperationType.USEPROFILE_P)) {
				response = TransactionService.getInstance().getUserProfileByPhone(request);
			} else if (operationType.equals(AppConstant.OperationType.UPDATEPROFILE)) {
				response = TransactionService.getInstance().updateUserProfile(request);
			} else if (operationType.equals(AppConstant.OperationType.BURNPOINT)) {
				response = TransactionService.getInstance().burnPoints(request);
			} else if (operationType.equals(AppConstant.OperationType.EARNPOINT)) {
				response = TransactionService.getInstance().earnPoints(request);
			} else if (operationType.equals(AppConstant.OperationType.ADDPOINT)) {
				response = TransactionService.getInstance().addPoints(request);
			} else if (operationType.equals(AppConstant.OperationType.REISSUECARD)) {
				response = TransactionService.getInstance().reissueCard(request);
				//TODO - reversal and void are not supported for now. Switch will call earnpoint/burn point to achieve this.
			} else if (operationType.equals(AppConstant.OperationType.REVERSE)) {
				//this is reversal
			} else if (operationType.equals(AppConstant.OperationType.CANCEL)) {
				//this is void
			} else if (operationType.equals(AppConstant.OperationType.TXNHISTORY)) {
				//TODO - Txn history is not supported at issuer. Switch will get the txn history from oltp table and send back to client.
			} else if (operationType.equals(AppConstant.OperationType.DEACTIVATE)) {
				response = TransactionService.getInstance().deactivateCard(request);
			}
			if(response==null){
				response = prepareFailureResponse();
			}
			populateResponseMsg(response);
		} catch (Exception e) {
			e.printStackTrace();
			response = new Response();
			response.setResponseCode(ResponseCode.INTERNAL_FAILURE);
		}
		try {
			System.out.println("Response sent : "+mapper.writeValueAsString(response));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	private Response prepareFailureResponse(){
		Response response = new Response();
		response.setResponseCode(ResponseCode.INTERNAL_FAILURE);
		return response;
	}

	private void populateResponseMsg(Response response) {
		if (responseCodeMap == null) {
			// load the response Code map
			try {
				responseCodeMap = new Properties();
				InputStream inputStream = TransactionProcessor.class.getClassLoader().getResourceAsStream("response_code.properties");
				responseCodeMap.load(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (response.getResponseMsg() == null)
			response.setResponseMsg((String)responseCodeMap.get(response.getResponseCode()));
	}
}
