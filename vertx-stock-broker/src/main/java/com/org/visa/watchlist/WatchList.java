package com.org.visa.watchlist;

import java.util.List;

import com.org.visa.assets.Asset;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Value
public class WatchList {

  List<Asset> assets;

  JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
