package com.thickedge.issuer.service;

import com.thickedge.issuer.core.CardHolder;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;

public class TransactionServiceTest {

	public static void main(String[] args) {
		Response response = null;
		Request request = getRequest();
		TransactionService service = TransactionService.getInstance();
		//service.registerUser(getRequest());
		//Response response  = service.getUserProfile(getRequest());
		
		response = service.getCardDetail(request);
		System.out.println(response.getPointsAvailable());
		
		response = service.earnPoints(request);
		System.out.println(response.getPointsAvailable());
		
		request.setPoints(10);
		response = service.burnPoints(request);
		System.out.println(response.getPointsAvailable());

		request.setPoints(30);
		response = service.addPoints(request);
		System.out.println(response.getPointsAvailable());
	}
	
	private static Request getRequest(){
		Request request = new Request();
		CardHolder cardHolder = new CardHolder();
		request.setCardHolder(cardHolder);
		cardHolder.setFirstName("Manoj");
		cardHolder.setLastName("Singh");
		request.setCardNumber("8888880018904563");
		request.setPoints(23);
		return request;
	}
}
