package com.org.visa.assets;

import java.util.Arrays;
import java.util.List;

import io.vertx.ext.web.Router;

public class AssetsRestApi {

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");

  public static void attachV2(Router parent) {
    parent.get("/assets").handler(new GetAssetsHandler());
  }

}
