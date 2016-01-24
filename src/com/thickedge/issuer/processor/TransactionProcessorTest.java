package com.thickedge.issuer.processor;

import org.codehaus.jackson.map.ObjectMapper;

import com.thickedge.issuer.constant.AppConstant.OperationType;
import com.thickedge.issuer.core.CardHolder;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;


public class TransactionProcessorTest {
	
	static TransactionProcessor processor = TransactionProcessor.getInstance();
	
	public static void main(String[] args) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Response response = null;
		//response = doRegistrationWithCard();
		//response = doRegistrationWithPhone();
		//response = earnPointByCard();
		//response = earnPointByPhone();
		//response = burnPointByCard();
		//response = burnPointByPhone();
		//response = getCardDetailByCard();
		//response = getCardDetailByPhone();
		//response = reissueCard();
		response = updateProfile();
		System.out.println("Response received ..."+ mapper.writeValueAsString(response));
	}

	static Response getCardDetailByCard() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setPoints(505);
		request.setCardNumber("8411808093854927");
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.USEPROFILE_C);
	}

	static Response getCardDetailByPhone() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setPoints(505);
		request.setPhoneNumber("9810403543");
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.USEPROFILE_P);
	}

	
	static Response reissueCard() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setPoints(505);
		request.setCardNumber("8888880018904563");
		request.setNewCardNumber("8888880018904570");
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.REISSUECARD);
	}

	
	static Response burnPointByPhone() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setPoints(90);
		request.setPhoneNumber("9810403549");
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.BURNPOINT);
	}

	static Response burnPointByCard() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		//request.setCardNumber("8888880018904563");
		request.setCardNumber("8888880018904654");
		request.setPoints(101);
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.BURNPOINT);
	}

	static Response doRegistrationWithCard() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setCardNumber("8888880018904123");
		request.setCardHolder(getCardHolder());
		request.getCardHolder().setPhoneNumber("9810403901");
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.REGISTERUSER);
	}

	static Response doRegistrationWithPhone() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		//request.setCardNumber("8888880018904563");
		request.setCardHolder(getCardHolder());
		request.getCardHolder().setPhoneNumber("9810403543");
		request.setUpc("1231231231231");
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.REGISTERUSER);
	}

	static Response earnPointByCard() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setCardNumber("8888880018904876");
		request.setPoints(230);
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.EARNPOINT);
	}

	static Response earnPointByPhone() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setPoints(505);
		request.setPhoneNumber("9810403549");
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.EARNPOINT);
	}

	static Response updateProfile() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		Request request = new Request();
		request.setMembershipId("6945141");
		CardHolder cardHolder = new CardHolder();
		cardHolder.setLastName("Singhania1");
		request.setCardHolder(cardHolder);
		String json = mapper.writeValueAsString(request);
		System.out.println("Request Sent :"+json);
		return processor.processTransaction(request, OperationType.UPDATEPROFILE);
	}


	/*
	 * {"merchantId":"10000000001","txnDate":"2015-08-17T18:18:21.305+08:00","terminalId":"10000000001","stan":"201508101781", 
	 * "cardNumber":"8888880018904568", "cardHolder":{"firstName":"Manoj","lastName":"Singh","gender":"M","dateOfBirth":"1982-06-29",
	 * "phoneNumber":"9810403543","email":"m.manojsingh@gmail.com","address":"Noida","city":"Noida","state":"UP","zip":"201301","country":"India"}}

	 */
	private static CardHolder getCardHolder() throws Exception{
		String json = "{\"firstName\":\"MKS\",\"lastName\":\"Singh\",\"gender\":\"M\",\"dateOfBirth\":\"1982-06-29\",\"phoneNumber\":\"8010551710\",\"email\":\"m.manojsingh@gmail.com\",\"address\":\"Noida\",\"city\":\"Noida\",\"state\":\"UP\",\"zip\":\"201301\",\"country\":\"India\"}";
		ObjectMapper mapper = new ObjectMapper();
		CardHolder cardHolder = mapper.readValue(json, CardHolder.class);
		System.out.println(cardHolder);
		return cardHolder;
	}
}
