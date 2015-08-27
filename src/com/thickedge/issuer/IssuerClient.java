package com.thickedge.issuer;

import java.util.Date;

import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;


public class IssuerClient {

    public static void main(String[] args) {


	String url = "http://localhost:8080/Issuer/process/issuer/api/";

	    IssuerCommunicator client = IssuerCommunicator.getInstance(url);
	    Request request = new Request();
	    request.setTxnAmount(100.0);
	    request.setMerchantId("22222222222");
	    request.setTerminalId("22222222222");
	    request.setTxnDate(new Date());
	    request.setCardNumber("8888888800006270");

	    Response response= client.invokeBalanceInquiry(request);
	    System.out.println(response.getResponseCode());
	    System.out.println(response.getBalance());
    }
}
