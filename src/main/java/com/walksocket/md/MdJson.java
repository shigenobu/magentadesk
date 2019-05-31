package com.walksocket.md;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * json.
 */
public class MdJson {

  /**
   * gson.
   */
  private static final Gson gson
      = new GsonBuilder()
      .excludeFieldsWithoutExposeAnnotation()
      .serializeNulls()
      .create();

  /**
   * gson pretty.
   */
  private static final Gson gsonPretty
      = new GsonBuilder()
      .excludeFieldsWithoutExposeAnnotation()
      .serializeNulls()
      .setPrettyPrinting()
      .create();

  /**
   * constructor.
   */
  private MdJson() {}

  /**
   * to object from json string.
   * @param json json string
   * @param cls binding class
   * @param <T> binding type
   * @return binding object
   */
  public static <T> T toObject(String json, Class<T> cls) {
    try {
      return gson.fromJson(json, cls);
    } catch (Exception e) {
      MdLogger.error(e);
    }
    return null;
  }

  /**
   * to json string from object.
   * @param src object
   * @return json string
   */
  public static String toJsonString(Object src) {
    try {
      return gson.toJson(src);
    } catch (Exception e) {
      MdLogger.error(e);
    }
    return null;
  }

  /**
   * to json string from object at friendly.
   * @param src object
   * @return json string
   */
  public static String toJsonStringFriendly(Object src) {
    try {
      return gsonPretty.toJson(src);
    } catch (Exception e) {
      MdLogger.error(e);
    }
    return null;
  }
}
