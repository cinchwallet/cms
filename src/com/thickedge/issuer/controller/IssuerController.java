package com.thickedge.issuer.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.thickedge.issuer.constant.AppConstant;
import com.thickedge.issuer.core.Request;
import com.thickedge.issuer.core.Response;
import com.thickedge.issuer.processor.TransactionProcessor;



@Path("/loyalty/api/")
public class IssuerController {


	@GET
	@Path("/serverstatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getServerStatus() {
		Response response = new Response();
		response.setResponseCode("0000");
		response.setResponseMsg("Connection with server is OK");
		return response;

	}
	
	@POST
	@Path("/registeruser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.REGISTERUSER);

	}

	@GET
	@Path("/carddetail/{cardNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCardDetail(@PathParam("cardNumber") String cardNumber) {
		Request request = new Request();
		request.setCardNumber(cardNumber);
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.CARDDETAIL);

	}

	@GET
	@Path("/userprofile/{cardNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserProfile(@PathParam("cardNumber") String cardNumber) {
		Request request = new Request();
		request.setCardNumber(cardNumber);
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.USEPROFILE);

	}

	@POST
	@Path("/burnpoint")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response burnPoint(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.BURNPOINT);

	}

	@POST
	@Path("/earnpoint")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response earnPoint(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.EARNPOINT);

	}

	@POST
	@Path("/addpoint")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPoint(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.ADDPOINT);

	}

	@POST
	@Path("/reissuecard")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reissueCard(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.REISSUECARD);

	}

	@POST
	@Path("/reverse")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reverseTxn(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.REVERSE);

	}

	@POST
	@Path("/cancel")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelTxn(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.CANCEL);

	}
	
	@POST
	@Path("/deactivate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deactivateCard(Request request) {
		return TransactionProcessor.getInstance().processTransaction(request, AppConstant.OperationType.DEACTIVATE);

	}

}