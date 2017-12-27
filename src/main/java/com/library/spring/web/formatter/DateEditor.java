package com.library.spring.web.formatter;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @see <a href="https://dzone.com/articles/how-serialize-javautildate">How to Serialize Java.util.Date with Jackson JSON Processor</a>
 * @see <a href="http://www.baeldung.com/jackson-serialize-dates">Jackson Date</a>
 * @see <a href="http://magicmonster.com/kb/prg/java/spring/webmvc/jackson_custom.html">Customise the Jackson JSON mapper</a>
 * @see <a href="https://github.com/FasterXML/jackson-docs/wiki/JacksonHowToCustomSerializers">Jackson How-To: Custom Serializers</a>
 * @see <a href="https://www.concretepage.com/spring/spring-mvc/spring-mvc-validator-with-initbinder-webdatabinder-registercustomeditor-example">@InitBinder</a>
 * @see <a href="How To create a global InitBinder in Spring with @ControllerAdvice">Create a global InitBinder in Spring with @ControllerAdvice</a>
 */
public class DateEditor extends PropertyEditorSupport {

    public static final String DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";

    public void setAsText(String value) {
        try {
            setValue(new SimpleDateFormat(DATE_FORMAT).parse(value));
        } catch (ParseException e) {
            setValue(null);
        }
    }

    public String getAsText() {
        String s = "";
        if (getValue() != null) {
            s = new SimpleDateFormat(DATE_FORMAT).format((Date) getValue());
        }
        return s;
    }
}