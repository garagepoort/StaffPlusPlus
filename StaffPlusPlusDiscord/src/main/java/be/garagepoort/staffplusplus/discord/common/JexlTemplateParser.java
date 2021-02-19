package be.garagepoort.staffplusplus.discord.common;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JexlTemplateParser {

    private static final String IF = "#IF";
    private static final String ENDIF = "#ENDIF";

    public static String parse(String template, JexlContext jexlContext) {
        HashMap<String, Object> ns = new HashMap<>();
        ns.put("utils", new JexlUtilities());

        JexlEngine jexl = new JexlBuilder()
            .namespaces(ns)
            .create();

        StringBuilder stringBuilder = new StringBuilder();
        boolean keepLine = true;
        for (String line : template.split("\n")) {
            line = line.trim();
            if (!line.startsWith(ENDIF) && !keepLine) {
                continue;
            }
            if (line.startsWith(ENDIF)) {
                keepLine = true;
                continue;
            }
            if (line.startsWith(IF)) {
                JexlExpression expression = jexl.createExpression(line.replace(IF, "").trim());
                boolean evaluate = (boolean) expression.evaluate(jexlContext);
                if (!evaluate) {
                    keepLine = false;
                }
                continue;
            }
            line = replaceInlineExpressions(jexlContext, jexl, line);
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private static String replaceInlineExpressions(JexlContext jexlContext, JexlEngine jexl, String line) {
        String regex = Pattern.quote("${") + "(.*?)" + Pattern.quote("}");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        while (m.find()) {
            JexlExpression expression = jexl.createExpression(m.group(1));
            String evaluate = expression.evaluate(jexlContext).toString();
            line = line.replaceFirst(regex, evaluate);
        }
        return line;
    }
}
