package com.squid.core.poi;

public class TooManyRowsException extends Exception {

  @Override
  public String getMessage() {
    return "The limit of rows in a MS Excel worksheet has been reached";
  }

}
