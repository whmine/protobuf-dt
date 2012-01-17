/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.util;

import static java.util.regex.Pattern.compile;
import static org.eclipse.xtext.util.Strings.isEmpty;

import java.util.regex.Pattern;

/**
 * Utility methods related to {@code String}.s
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public final class Strings {
  private static final Pattern LINE_BREAK = compile("\"[\t\r\n]+\"|'[\t\r\n]+'");

  /**
   * Returns a {@code String} containing the given one in double quotes.
   * @param s the given {@code String}, may be {@code null}.
   * @return a {@code String} containing the given one in double quotes, or {@code null} if the given {@code String} is
   * {@code null}.
   */
  public static String quote(String s) {
    if (s == null) {
      return s;
    }
    return "\"" + s + "\"";
  }

  /**
   * Removes surrounding quotes from the given {@code String}.
   * @param s the given {@code String}, may be {@code null}.
   * @return a {@code String} containing the given one without surrounding quotes, or {@code null} if the given
   * {@code String} is {@code null}.
   */
  public static String unquote(String s) {
    if (!isQuoted(s)) {
      return s;
    }
    return s.substring(1, s.length() - 1);
  }

  private static boolean isQuoted(String s) {
    if (isEmpty(s)) {
      return false;
    }
    return (s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"));
  }

  /**
   * Returns a {@code String} containing the given one without line breaks.
   * @param s the given {@code String}, may be {@code null}.
   * @return a {@code String} containing the given one without line breaks, or {@code null} if the given {@code String}
   * is {@code null}.
   */
  public static String removeLineBreaksFrom(String s) {
    if (isEmpty(s)) {
      return s;
    }
    return LINE_BREAK.matcher(s).replaceAll("");
  }
  private Strings() {}
}