package com.hgzp.utils.html;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgcb.utils.HtmlUtils
 * 创建人：peij
 * 类描述：HtmlUtils
 * 创建日期：2023/2/18 11:25
 */
public class HtmlUtils extends org.springframework.web.util.HtmlUtils {
    /**
     * Turn HTML character references into their plain text UNICODE equivalent.
     * <p>Handles complete character set defined in HTML 4.01 recommendation
     * and all reference types (decimal, hex, and entity).
     * <p>Correctly converts the following formats:
     * <blockquote>
     * &amp;#<i>Entity</i>; - <i>(Example: &amp;amp;) case sensitive</i>
     * &amp;#<i>Decimal</i>; - <i>(Example: &amp;#68;)</i><br>
     * &amp;#x<i>Hex</i>; - <i>(Example: &amp;#xE5;) case insensitive</i><br>
     * </blockquote>
     * <p>Gracefully handles malformed character references by copying original
     * characters as is when encountered.
     * <p>Reference:
     * <a href="https://www.w3.org/TR/html4/sgml/entities.html">
     * https://www.w3.org/TR/html4/sgml/entities.html
     * </a>
     * @param input the (escaped) input string | null
     * @return the unescaped string
     */
    public static String htmlUnescape(String input) {
        if(null == input){
            return null;
        }
        return org.springframework.web.util.HtmlUtils.htmlUnescape(input);
    }
}
