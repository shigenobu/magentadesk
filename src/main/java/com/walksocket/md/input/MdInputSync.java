package com.walksocket.md.input;

import com.google.gson.annotations.Expose;
import com.walksocket.md.MdBash;
import com.walksocket.md.MdUtils;
import com.walksocket.md.exception.MdExceptionInvalidInput;
import com.walksocket.md.http.MdHttpClient;
import com.walksocket.md.input.member.MdInputMemberCommand;
import com.walksocket.md.input.member.MdInputMemberHttp;

import java.util.ArrayList;
import java.util.List;

/**
 * input sync.
 */
public class MdInputSync extends MdInputAbstract {

  /**
   * summary id.
   */
  @Expose
  public String summaryId;

  /**
   * diff seqs.
   */
  @Expose
  public List<Long> diffSeqs;

  /**
   * run.
   */
  @Expose
  public boolean run;

  /**
   * force.
   */
  @Expose
  public boolean force;

  /**
   * loose.
   */
  @Expose
  public boolean loose;

  /**
   * commands just before commit.
   */
  @Expose
  public List<MdInputMemberCommand> commandsBeforeCommit;

  /**
   * commands just after commit.
   */
  @Expose
  public List<MdInputMemberCommand> commandsAfterCommit;

  /**
   * http callback just before commit.
   */
  @Expose
  public List<MdInputMemberHttp> httpCallbackBeforeCommit;

  /**
   * http callback just after commit.
   */
  @Expose
  public List<MdInputMemberHttp> httpCallbackAfterCommit;

  @Override
  public void validate() throws MdExceptionInvalidInput {
    super.validate();

    if (MdUtils.isNullOrEmpty(summaryId)) {
      throw new MdExceptionInvalidInput("Invalid summaryId.");
    }

    if (diffSeqs == null) {
      diffSeqs = new ArrayList<>();
    }

    if (commandsBeforeCommit == null) {
      commandsBeforeCommit = new ArrayList<>();
    }
    for (MdInputMemberCommand cmd : commandsBeforeCommit) {
      if (MdUtils.isNullOrEmpty(cmd.command)) {
        throw new MdExceptionInvalidInput("Invalid before command.");
      }
      if (cmd.timeout <= 0) {
        cmd.timeout = MdBash.DEFAULT_TIMEOUT;
      }
    }

    if (commandsAfterCommit == null) {
      commandsAfterCommit = new ArrayList<>();
    }
    for (MdInputMemberCommand cmd : commandsAfterCommit) {
      if (MdUtils.isNullOrEmpty(cmd.command)) {
        throw new MdExceptionInvalidInput("Invalid after command.");
      }
      if (cmd.timeout <= 0) {
        cmd.timeout = MdBash.DEFAULT_TIMEOUT;
      }
    }

    if (httpCallbackBeforeCommit == null) {
      httpCallbackBeforeCommit = new ArrayList<>();
    }
    for (MdInputMemberHttp http : httpCallbackBeforeCommit) {
      if (MdUtils.isNullOrEmpty(http.url)) {
        throw new MdExceptionInvalidInput("Invalid before url.");
      }
      if (http.timeout <= 0) {
        http.timeout = MdHttpClient.DEFAULT_TIMEOUT;
      }
    }

    if (httpCallbackAfterCommit == null) {
      httpCallbackAfterCommit = new ArrayList<>();
    }
    for (MdInputMemberHttp http : httpCallbackAfterCommit) {
      if (MdUtils.isNullOrEmpty(http.url)) {
        throw new MdExceptionInvalidInput("Invalid after url.");
      }
      if (http.timeout <= 0) {
        http.timeout = MdHttpClient.DEFAULT_TIMEOUT;
      }
    }
  }

  @Override
  public Mode getMode() {
    return MdInputAbstract.Mode.SYNC;
  }
}
