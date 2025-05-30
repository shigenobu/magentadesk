package com.walksocket.md.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.context.Context;

/**
 * template handler.
 */
public class MdTemplateHandler implements ReferenceInsertionEventHandler {

  /**
   * non escape Strings.
   */
  private static final List<String> nonEscapes = new ArrayList<>();

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
        .filter(reference::startsWith)
        .findFirst();
    if (opt.isPresent()) {
      return value;
    }
    return StringEscapeUtils.escapeXml11(value.toString());
  }
}
