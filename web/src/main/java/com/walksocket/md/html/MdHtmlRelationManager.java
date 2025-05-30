package com.walksocket.md.html;

import com.walksocket.md.MdLogger;
import com.walksocket.md.MdUtils;
import com.walksocket.md.input.member.MdInputMemberRelation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * html relation manager.
 */
public class MdHtmlRelationManager {

  /**
   * relation list.
   */
  private final List<MdHtmlRelation> relationList = new ArrayList<>();

  /**
   * add.
   * <pre>
   *   (sample from => to)
   *   'news_i18n.news_id' => 'news.news_id'
   *   'news_i18n.image_id' => 'image.id'
   *   'news_i18n_type.news_id,language' => 'news_i18n.news_id,language'
   *   'card.chara_id' => 'chara.chara_id'
   *   'banner.image_id' => 'image.id'
   *   'pass.battle_id' => 'battle.battle_id'
   *   'tier.battle_id' => 'battle.battle_id'
   *   'bonus.battle_id' => 'battle.battle_id'
   * </pre>
   *
   * @param relation relation
   */
  public void add(MdInputMemberRelation relation) {
    String foreign = relation.from;
    String reference = relation.to;
    MdLogger.trace(String.format("foreign:%s", foreign));
    MdLogger.trace(String.format("reference:%s", reference));

    // table
    String[] tmpForeigns = foreign.split(Pattern.quote("."));
    if (MdUtils.isNullOrEmpty(tmpForeigns)) {
      return;
    }
    String table = tmpForeigns[0];
    MdLogger.trace(String.format("table:%s", table));

    // foreignKeys
    if (tmpForeigns.length < 2) {
      return;
    }
    String[] tmpForeignKeys = tmpForeigns[1].split(Pattern.quote(","));
    if (MdUtils.isNullOrEmpty(tmpForeignKeys)) {
      return;
    }
    List<String> foreignKeys = new ArrayList<>();
    for (String tmpForeignKey : tmpForeignKeys) {
      foreignKeys.add(tmpForeignKey.trim());
    }
    MdLogger.trace(
        String.format("foreignKeys:%s", String.join(",", foreignKeys)));

    // referenceTable
    String[] tmpReferences = reference.split(Pattern.quote("."));
    if (MdUtils.isNullOrEmpty(tmpReferences)) {
      return;
    }
    String referenceTable = tmpReferences[0];
    MdLogger.trace(String.format("referenceTable:%s", referenceTable));

    // referenceKeys
    if (tmpReferences.length < 2) {
      return;
    }
    String[] tmpReferenceKeys = tmpReferences[1].split(Pattern.quote(","));
    if (MdUtils.isNullOrEmpty(tmpReferenceKeys)) {
      return;
    }
    List<String> referenceKeys = new ArrayList<>();
    for (String referenceKey : tmpReferenceKeys) {
      referenceKeys.add(referenceKey.trim());
    }
    MdLogger.trace(
        String.format("referenceKeys:%s", String.join(",", referenceKeys)));

    relationList.add(new MdHtmlRelation(table, foreignKeys, referenceTable, referenceKeys));
  }

  /**
   * get relation list.
   *
   * @return get relation list
   */
  public List<MdHtmlRelation> getRelationList() {
    return relationList;
  }

  /**
   * get relation list for table.
   *
   * @param table table
   * @return get relation list
   */
  public List<MdHtmlRelation> getRelationListForTable(String table) {
    return relationList.stream().filter(r -> r.referenceTable.equals(table))
        .collect(Collectors.toList());
  }

  /**
   * get link target.
   *
   * @param table       table
   * @param columnNames columnNames
   * @param values      values
   * @return link
   */
  public String getLinkTarget(String table, List<String> columnNames, List<String> values) {
    List<MdHtmlRelation> relations = relationList.stream()
        .filter(r -> r.referenceTable.equals(table))
        .toList();
    if (MdUtils.isNullOrEmpty(relations)) {
      return "";
    }

    List<String> links = new ArrayList<>();
    for (MdHtmlRelation r : relations) {
      Map<String, String> filteredValues = new HashMap<>();
      for (int i = 0; i < columnNames.size(); i++) {
        String name = columnNames.get(i);
        if (r.referenceKeys.contains(name) && values.size() > i) {
          filteredValues.put(name, values.get(i));
        }
      }
      if (MdUtils.isNullOrEmpty(filteredValues)) {
        continue;
      }

      links.add(MdUtils.getHash(
          String.format(
              "%s.%s.%s",
              r.table,
              String.join(",", r.foreignKeys),
              String.join(",", filteredValues.values()
                  .stream()
                  .toList()))));
    }
    if (MdUtils.isNullOrEmpty(links)) {
      return "";
    }

    return String.format("data-target=%s", String.join("|", links));
  }

  /**
   * get link id.
   *
   * @param table       table
   * @param columnNames columnNames
   * @param values      values
   * @return link
   */
  public String getLinkId(String table, List<String> columnNames, List<String> values) {
    List<MdHtmlRelation> relations = relationList.stream()
        .filter(r -> r.table.equals(table))
        .toList();
    if (MdUtils.isNullOrEmpty(relations)) {
      return "";
    }

    List<String> links = new ArrayList<>();
    for (MdHtmlRelation r : relations) {
      Map<String, String> filteredValues = new HashMap<>();
      for (int i = 0; i < columnNames.size(); i++) {
        String name = columnNames.get(i);
        if (r.foreignKeys.contains(name) && values.size() > i) {
          filteredValues.put(name, values.get(i));
        }
      }
      if (MdUtils.isNullOrEmpty(filteredValues)) {
        continue;
      }

      links.add(MdUtils.getHash(
          String.format(
              "%s.%s.%s",
              r.table,
              String.join(",", r.foreignKeys),
              String.join(",", filteredValues.values()
                  .stream()
                  .toList()))));
    }
    if (MdUtils.isNullOrEmpty(links)) {
      return "";
    }

    return String.format("data-id=%s", String.join("|", links));
  }
}
