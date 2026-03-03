package com.org.visa.watchlist;

import java.util.HashMap;
import java.util.UUID;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PutWatchListHandler implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(PutWatchListHandler.class);
  private final HashMap<UUID, WatchList> watchListPerAccount;

  public PutWatchListHandler(final HashMap<UUID, WatchList> watchListPerAccount) {
    this.watchListPerAccount = watchListPerAccount;
  }

  @Override
  public void handle(final RoutingContext context) {
    String accountId = WatchListRestApi.getAccountId(context);

    var json = context.getBodyAsJson();
    if (json == null) {
      context.response() .setStatusCode(400) .end("Request body must be JSON");
      return;
    }
    var UuId = UUID.randomUUID();
    LOG.info("Get randomUUID: {} ", UuId);
    LOG.info("Get Request: {} ", json);
    var watchList = json.mapTo(WatchList.class);
    watchListPerAccount.put(UUID.fromString(accountId), watchList);
    context.response().end(json.toBuffer());
  }
}
