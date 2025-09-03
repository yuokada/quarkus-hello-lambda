package io.github.yuokada.lambda.model;

public class OutputResponse {

  String name;
  String message;

  public String getName() {
    return name;
  }

  public OutputResponse setName(String name) {
    this.name = name;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public OutputResponse setMessage(String message) {
    this.message = message;
    return this;
  }
}
