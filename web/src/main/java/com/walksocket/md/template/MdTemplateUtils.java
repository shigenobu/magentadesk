package com.walksocket.md.template;

import com.walksocket.md.MdUtils;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * template utils.
 */
public class MdTemplateUtils {

  /**
   * selected.
   *
   * @param base    base
   * @param compare compare
   * @return selected="selected" or empty
   */
  public static String selected(Object base, Object compare) {
    if (base == null || compare == null) {
      return "";
    }
    if (base.toString().equals(compare.toString())) {
      return "selected=\"selected\"";
    }
    return "";
  }

  /**
   * selected.
   *
   * @param base    base
   * @param compare compare
   * @return selected="selected" or empty
   */
  public static String selected(Object base, List<Object> compare) {
    if (base == null || compare == null) {
      return "";
    }
    if (compare.contains(base.toString())) {
      return "selected=\"selected\"";
    }
    return "";
  }

  /**
   * selected.
   *
   * @param base    base
   * @param compare compare
   * @return selected="selected" or empty
   */
  public static String selected(List<Object> base, Object compare) {
    if (base == null || compare == null) {
      return "";
    }
    if (base.contains(compare.toString())) {
      return "selected=\"selected\"";
    }
    return "";
  }

  /**
   * checked.
   *
   * @param base    base
   * @param compare compare
   * @return checked="checked" or empty
   */
  public static String checked(Object base, Object compare) {
    if (base == null || compare == null) {
      return "";
    }
    if (base.toString().equals(compare.toString())) {
      return "checked=\"checked\"";
    }
    return "";
  }

  /**
   * checked.
   *
   * @param base    base
   * @param compare compare
   * @return checked="checked" or empty
   */
  public static String checked(Object base, List<Object> compare) {
    if (base == null || compare == null) {
      return "";
    }
    if (compare.contains(base.toString())) {
      return "checked=\"checked\"";
    }
    return "";
  }

  /**
   * checked.
   *
   * @param base    base
   * @param compare compare
   * @return checked="checked" or empty
   */
  public static String checked(List<Object> base, Object compare) {
    if (base == null || compare == null) {
      return "";
    }
    if (base.contains(compare.toString())) {
      return "checked=\"checked\"";
    }
    return "";
  }

  /**
   * readonly.
   *
   * @return readonly="readonly" or empty
   */
  public static String readonly() {
    return "readonly=\"readonly\"";
  }

  /**
   * disabled.
   *
   * @return disabled="disabled" or empty
   */
  public static String disabled() {
    return "disabled=\"disabled\"";
  }

  /**
   * non escape.
   *
   * @param value value
   * @return non escape value
   */
  public static String nonEscape(Object value) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

  /**
   * line break to br.
   *
   * @param value value
   * @return value replaced to <br />
   */
  public static String nl2br(Object value) {
    if (value == null) {
      return "";
    }
    String valueString = value.toString();
    valueString = valueString.replaceAll("\r", "");
    valueString = valueString.replaceAll("\n", "<br />");
    return valueString;
  }

  /**
   * get hash.
   *
   * @param src src
   * @return hash
   */
  public static String getHash(String src) {
    return MdUtils.getHash(src);
  }

  /**
   * is null.
   *
   * @param src src
   * @return if null, return true
   */
  public static boolean isNull(Object src) {
    return src == null;
  }

  /**
   * is all null.
   *
   * @param src src
   * @return if all null, return true
   */
  public static boolean isAllNull(Collection src) {
    return src.stream().allMatch(Objects::isNull);
  }

  /**
   * is not null.
   *
   * @param src src
   * @return if not null, return true
   */
  public static boolean isNotNull(Object src) {
    return src != null;
  }
}
