package com.thickedge.issuer.constant;

public class AppConstant {

	public enum CardStatus {
		OPEN(1), CLOSED(0), NEW(2);
		private int code;

		private CardStatus(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}
	}

	public enum OperationType {

		SERVERSTATUS("SERVERSTATUS"),
		CARDDETAIL("CARDDETAIL"),
		USEPROFILE("USEPROFILE"),
		BALANCE("BALANCE"),
		EARNPOINT("EARNPOINT"),
		BURNPOINT("BURNPOINT"),
		ADDPOINT("ADDPOINT"),
		REGISTERUSER("REGISTERUSER"),
		DEACTIVATE("DEACTIVATE"),
		REISSUECARD("REISSUECARD"),
		REVERSE("REVERSE"),
		CANCEL("CANCEL"),
		TXNHISTORY("TXNHISTORY");

		private String type;

		private OperationType(String oprType) {
			type = oprType;
		}

		public String getType() {
			return type;
		}
	}
}
