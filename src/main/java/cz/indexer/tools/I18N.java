package cz.indexer.tools;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.*;

/**
 * Internationalization (I18N) class
 * Currently supported languages: English, Czech.
 * Based on https://javafxpedia.com/en/tutorial/5434/internationalization-in-javafx
 */
public final class I18N {

	private static final ObjectProperty<Locale> locale;

	static {
		locale = new SimpleObjectProperty<>(getDefaultLocale());
		locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
	}

	public static List<Locale> getSupportedLocales() {
		return new ArrayList<>(Arrays.asList(Locale.ENGLISH, new Locale("cs", "CZ")));
	}

	public static Locale getDefaultLocale() {
		Locale sysDefault = Locale.getDefault();
		return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
	}

	public static Locale getLocale() {
		return locale.get();
	}

	public static void setLocale(Locale newLocale) {
		locale.set(newLocale);
		Locale.setDefault(newLocale);
	}

	public static StringBinding createStringBinding(final String key, Object... args) {
		return Bindings.createStringBinding(() -> get(key, args), locale);
	}

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