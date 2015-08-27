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
}
