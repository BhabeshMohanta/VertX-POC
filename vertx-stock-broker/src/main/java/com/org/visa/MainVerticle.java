package com.org.visa;

import com.org.visa.assets.AssetsRestApi;
import com.org.visa.watchlist.WatchListRestApi;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error ->
      LOG.error("Unhandled:", error)
    );
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> LOG.error("Failed to deploy:", err))
      .onSuccess(id ->
        LOG.info("Deployed {} with id {}", MainVerticle.class.getSimpleName(), id)
      );
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    final Router restApi = Router.router(vertx);
    restApi.route().handler(BodyHandler.create());
    AssetsRestApi.attachV2(restApi);
    WatchListRestApi.attachV2(restApi);

   /* restApi.get("/assets").handler(context ->{
      final JsonArray response =new JsonArray();
      response.add(new JsonObject().put("symbal", "AAPL"))
      .add(new JsonObject().put("symbal", "AMZN"))
      .add(new JsonObject().put("symbal", "NTFX"))
      .add(new JsonObject().put("symbal", "TSLA"));
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });*/

    vertx.createHttpServer().requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("HTTP Server Error: ", error))
      .listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }



  /*@Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(VersionInfoVerticle.class.getName())
      .onFailure(startPromise::fail)
      .onSuccess(id -> LOG.info("Deployed {} with id {}", VersionInfoVerticle.class.getSimpleName(), id))
     // .compose(next -> migrateDatabase())
      .onFailure(startPromise::fail)
      .onSuccess(id -> LOG.info("Migrated db schema to latest version!"))
      .compose(next -> deployRestApiVerticle(startPromise));
  }*/

 /* private Future<Void> migrateDatabase() {
    return ConfigLoader.load(vertx)
      .compose(config -> {
        return FlywayMigration.migrate(vertx, config.getDbConfig());
      });
  }*/

  /*private Future<String> deployRestApiVerticle(final Promise<Void> startPromise) {
    return vertx.deployVerticle(RestApiVerticle.class.getName(),
      new DeploymentOptions().setInstances(halfProcessors())
    )
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        LOG.info("Deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }*/

  private int halfProcessors() {
    return Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
  }

}
