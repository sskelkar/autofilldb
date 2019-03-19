package com.github.sskelkar.autofilldb;

import org.junit.Test;

import static com.github.sskelkar.autofilldb.Util.removeQuotes;
import static org.junit.Assert.assertEquals;

public class UtilTest {

  @Test
  public void shouldRemoveQuotesAroundAString() {
    assertEquals("test", removeQuotes("'test'"));
    assertEquals("tes't", removeQuotes("'tes't'"));
    assertEquals("tes\'t", removeQuotes("'tes\'t'"));
    assertEquals("te'st", removeQuotes("te'st"));
    assertEquals("test", removeQuotes("test"));
    assertEquals("'test", removeQuotes("'test"));
    assertEquals("test'", removeQuotes("test'"));
    assertEquals("", removeQuotes("''"));
    assertEquals("'", removeQuotes("'"));
  }
}