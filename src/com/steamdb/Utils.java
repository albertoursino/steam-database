package com.steamdb;

public class Utils {
    enum LabelAlignment {left, right, center}

    /**
     * format messages such that JLabels displaying it contain at most breakLen chars for each line.
     * Text is also aligned according to the specified parameter and its color is set to {@code color}.
     *
     * @param msg       to be formatted
     * @param breakLen  number of chars for each line, <= 0 to indicate no line breaks
     * @param alignment specifies the text-align style
     * @param color     of the text
     * @return the formatted html message
     */
    static String htmlStyleStr(String msg, int breakLen, LabelAlignment alignment, String color) {
        String head = "<html>";
        if (alignment != null) head += "<p style='text-align:" + alignment.toString() + "'";
        if (color != null) head += " color='" + color + "'>";
        String end = "</p></html>";
        if (breakLen <= 0) return head + msg + end;
        StringBuilder builder = new StringBuilder(head);
        int msgLen = msg.length();
        int lineBreaks = msgLen / breakLen;
        int i = 0;
        for (; i < lineBreaks; i++) {
            builder.append(msg.substring(i * breakLen, (i + 1) * breakLen));
            builder.append("<br>");
        }
        builder.append(msg.substring(i * breakLen));
        builder.append(end);
        return builder.toString();
    }
}
