package com.lisantra.arabicbasic;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/** Loads {@code InterpreterMessages} for the interpreter message locale and formats with {@link MessageFormat}. */
public final class RuntimeMessages {

  private static final String BUNDLE = "InterpreterMessages";

  private final ResourceBundle bundle;
  private final Locale locale;

  public RuntimeMessages(Locale locale) {
    this.locale = Objects.requireNonNull(locale);
    this.bundle =
        ResourceBundle.getBundle(
            BUNDLE,
            locale,
            RuntimeMessages.class.getClassLoader(),
            // Don't fall back to the JVM default locale (App sets it to Arabic) before the
            // English base bundle.
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
  }

  public Locale getLocale() {
    return locale;
  }

  public String format(String key, Object... args) {
    try {
      String pattern = bundle.getString(key);
      MessageFormat mf = new MessageFormat(pattern, locale);
      return mf.format(args);
    } catch (MissingResourceException e) {
      return key;
    }
  }

  /**
   * Full user-facing message: optional localized source prefix plus body from {@code key}.
   */
  public String fullMessage(DeclarationSite site, String key, Object... args) {
    String body = format(key, args);
    if (site.isUnknown()) {
      return body;
    }
    String prefix = format("error.sourceLocation", site.line(), site.charPositionInLine() + 1);
    return prefix + body;
  }
}
