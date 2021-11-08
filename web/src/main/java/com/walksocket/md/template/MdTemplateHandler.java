package com.walksocket.md.template;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * template handler.
 */
public class MdTemplateHandler implements ReferenceInsertionEventHandler {

  /**
   * non escape Strings.
   */
  private static List<String> nonEscapes = new ArrayList<String>();

  static {
    nonEscapes.add("${" + MdTemplateUtils.class.getSimpleName() + ".");
    nonEscapes.add("$" + MdTemplateUtils.class.getSimpleName() + ".");
  }

  @Override
  public Object referenceInsert(Context context, String reference, Object value) {
    if (value == null) {
      return "";
    }
    Optional<String> opt = nonEscapes.stream()
        .filter(nonEscape -> reference.startsWith(nonEscape))
        .findFirst();
    if (opt.isPresent()) {
      return value;
    }
    return StringEscapeUtils.escapeXml11(value.toString());
  }
}
