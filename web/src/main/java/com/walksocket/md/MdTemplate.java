package com.walksocket.md;

import com.walksocket.md.template.MdTemplateHandler;
import com.walksocket.md.template.MdTemplateUtils;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * template.
 */
public class MdTemplate {

  /**
   * template path.
   * <pre>
   * テンプレートのパス
   * </pre>
   */
  private final String path;

  /**
   * velocity engine.
   */
  private final VelocityEngine engine = new VelocityEngine();

  /**
   * vars for assign.
   * <pre>
   * アサインする変数
   * </pre>
   */
  private VelocityContext context = new VelocityContext();

  /**
   * constructor for class loader.
   *
   * @param path template path
   */
  public MdTemplate(String path) {
    this.path = path;

    Properties prop = new Properties();
    prop.setProperty("resource.default_encoding", StandardCharsets.UTF_8.name());
    prop.setProperty("output.encoding", StandardCharsets.UTF_8.name());
    prop.setProperty("resource.loaders", "class");
    prop.setProperty("class.resource.loader.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    prop.setProperty("runtime.log.logsystem.class",
        "org.apache.velocity.runtime.log.NullLogSystem");
    prop.setProperty("event_handler.reference_insertion.class",
        MdTemplateHandler.class.getName());
    engine.init(prop);
  }

  /**
   * constructor for file loader.
   *
   * @param basePath base path of template
   * @param path     template path
   */
  public MdTemplate(String basePath, String path) {
    this.path = path;

    Properties prop = new Properties();
    prop.setProperty("resource.default_encoding", StandardCharsets.UTF_8.name());
    prop.setProperty("output.encoding", StandardCharsets.UTF_8.name());
    prop.setProperty("resource.loaders", "file");
    prop.setProperty("resource.loader.file.class",
        "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
    prop.setProperty("resource.loader.file.path", basePath);
    prop.setProperty("runtime.log.logsystem.class",
        "org.apache.velocity.runtime.log.NullLogSystem");
    prop.setProperty("event_handler.reference_insertion.class",
        MdTemplateHandler.class.getName());
    engine.init(prop);
  }

  /**
   * assign var.
   *
   * @param key   key
   * @param value value
   */
  public void assign(String key, Object value) {
    context.put(key, value);
  }

  /**
   * clear assigned vars.
   */
  public void clearValues() {
    context = new VelocityContext();
  }

  /**
   * rendering template.
   *
   * @return evaluated string
   */
  public String render() {
    context.put(MdTemplateUtils.class.getSimpleName(), MdTemplateUtils.class);

    try {
      StringWriter writer = new StringWriter();
      Template template = engine.getTemplate(path, StandardCharsets.UTF_8.name());
      template.merge(context, writer);
      writer.close();
      return writer.toString();
    } catch (IOException e) {
      MdLogger.error(e);
    }
    return "";
  }
}
