package cz.indexer.tools;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.*;

/**
 * Internationalization (I18N) class.
 * Currently supported languages: English, Czech.
 * Based on https://javafxpedia.com/en/tutorial/5434/internationalization-in-javafx
 */
public final class I18N {

	/**
	 * Locale which is currently set.
	 */
	private static final ObjectProperty<Locale> locale;

	static {
		locale = new SimpleObjectProperty<>(getDefaultLocale());
		locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
	}

	/**
	 * Returns list of all supported locales.
	 * @return list of locales
	 */
	public static List<Locale> getSupportedLocales() {
		return new ArrayList<>(Arrays.asList(Locale.ENGLISH, new Locale("cs", "CZ")));
	}

	/**
	 * Returns default locale.
	 * @return default locale
	 */
	public static Locale getDefaultLocale() {
		Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
	}

	/**
	 * Get locale, which is set at the moment.
	 * @return locale
	 */
	public static Locale getLocale() {
		return locale.get();
	}

	/**
	 * Set localization of the program to the new locale.
	 * @param newLocale new locale to be set
	 */
	public static void setLocale(Locale newLocale) {
		locale.set(newLocale);
		Locale.setDefault(newLocale);
	}

	/**
	 * Creates  binding to the string.
	 * @param key key to the localized string
	 * @param args optional arguments
	 * @return binding to the localized string
	 */
	public static StringBinding createStringBinding(final String key, Object... args) {
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}

	/**
	 * Gets the string with the given key from the resource bundle for the current locale and uses it as first argument
	 * to MessageFormat.format, passing in the optional args and returning the result.
	 *
	 * @param key message key
	 * @param args optional arguments for the message
	 * @return localized formatted string
	 */
	public static String get(final String key, final Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages", getLocale());
		return MessageFormat.format(bundle.getString(key), args);
	}

	public static String getMessage(String key, Object ... arguments) {
		return get(key, arguments);
	}

	public static String getMessage(Enum<?> enumVal) {
		return getMessage(enumVal.toString().toLowerCase());
	}

	public static ResourceBundle getBundle() {
		return ResourceBundle.getBundle("messages", getLocale());
	}
}