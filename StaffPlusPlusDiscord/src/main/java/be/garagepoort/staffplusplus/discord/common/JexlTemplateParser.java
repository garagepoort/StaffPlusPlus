package be.garagepoort.staffplusplus.discord.common;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JexlTemplateParser {

    public static String parse(String template, JexlContext jexlContext) {
        JexlEngine jexl = new JexlBuilder().create();

        StringBuilder stringBuilder = new StringBuilder();
        boolean keepLine = true;
        for (String line : template.split("\n")) {
            line = line.trim();
            if (!line.startsWith("##") && !keepLine) {
                continue;
            }
            if (line.startsWith("##")) {
                keepLine = true;
                continue;
            }
            if (line.startsWith("#")) {
                JexlExpression expression = jexl.createExpression(line.replace("#", "").trim());
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
        Pattern p = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher m = p.matcher(line);
        while (m.find()) {
            JexlExpression expression = jexl.createExpression(m.group(1));
            String evaluate = expression.evaluate(jexlContext).toString();
            line = line.replaceAll("(\\$\\{)[^&]*(\\})", evaluate);
        }
        return line;
    }
}
