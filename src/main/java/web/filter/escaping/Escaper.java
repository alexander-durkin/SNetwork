package web.filter.escaping;

public class Escaper {

    public static String escape(String s) {

        int n = s.length();
        StringBuilder sb = new StringBuilder(n + 16);
        int pos = 0;
        for (int i = 0; i < n; ++i) {
            char ch = s.charAt(i);
            String repl = null;

            switch (ch) {
                case '"':   repl = "&#34;";  break;
                case '#':   repl = "&#35;";  break;
                case '$':   repl = "&#36;";  break;
                case '%':   repl = "&#37;";  break;
                case '&':   repl = "&#38;";  break;
                case '\'':  repl = "&#39;";  break;
                case '(':   repl = "&#40;";  break;
                case ')':   repl = "&#41;";  break;
                case '*':   repl = "&#42;";  break;
                case '+':   repl = "&#43;";  break;
                //case '-':   repl = "&#45;";  break;
                case '/':   repl = "&#47;";  break;
                case ';':   repl = "&#59;";  break;
                case '<':   repl = "&#60;";  break;
                case '=':   repl = "&#61;";  break;
                case '>':   repl = "&#62;";  break;
                case '{':   repl = "&#123;"; break;
                case '|':   repl = "&#124;"; break;
                case '}':   repl = "&#125;"; break;

                default: break;
            }

            if (repl != null) {
                sb.append(s, pos, i).append(repl);
                pos = i + 1;
            }
        }
        return sb.append(s, pos, n).toString();
    }

    public static String escapeQuery(String s) {

        int n = s.length();
        StringBuilder sb = new StringBuilder(n + 16);
        int pos = 0;
        for (int i = 0; i < n; ++i) {
            char ch = s.charAt(i);
            String repl = null;

            switch (ch) {
                case '"':   repl = "&#34;";  break;
                case '#':   repl = "&#35;";  break;
                case '$':   repl = "&#36;";  break;
                case '%':   repl = "&#37;";  break;
                case '\'':  repl = "&#39;";  break;
                case '(':   repl = "&#40;";  break;
                case ')':   repl = "&#41;";  break;
                case '*':   repl = "&#42;";  break;
                case '+':   repl = "&#43;";  break;
                //case '-':   repl = "&#45;";  break;
                case '/':   repl = "&#47;";  break;
                case ';':   repl = "&#59;";  break;
                case '<':   repl = "&#60;";  break;
                case '>':   repl = "&#62;";  break;
                case '{':   repl = "&#123;"; break;
                case '|':   repl = "&#124;"; break;
                case '}':   repl = "&#125;"; break;

                default: break;
            }

            if (repl != null) {
                sb.append(s, pos, i).append(repl);
                pos = i + 1;
            }
        }
        return sb.append(s, pos, n).toString();
    }

    public static String escapeHeader(String s) {

        int n = s.length();
        StringBuilder sb = new StringBuilder(n + 16);
        int pos = 0;
        for (int i = 0; i < n; ++i) {
            char ch = s.charAt(i);
            String repl = null;

            switch (ch) {
                case '"':   repl = "&#34;";  break;
                case '#':   repl = "&#35;";  break;
                case '$':   repl = "&#36;";  break;
                case '%':   repl = "&#37;";  break;
                case '&':   repl = "&#38;";  break;
                case '\'':  repl = "&#39;";  break;
                case '(':   repl = "&#40;";  break;
                case ')':   repl = "&#41;";  break;
                case '*':   repl = "&#42;";  break;
                case '+':   repl = "&#43;";  break;
                //case '-':   repl = "&#45;";  break;
                case ';':   repl = "&#59;";  break;
                case '<':   repl = "&#60;";  break;
                case '>':   repl = "&#62;";  break;
                //case '@':   repl = "&#64;";  break;
                case '{':   repl = "&#123;"; break;
                case '|':   repl = "&#124;"; break;
                case '}':   repl = "&#125;"; break;

                default: break;
            }

            if (repl != null) {
                sb.append(s, pos, i).append(repl);
                pos = i + 1;
            }
        }
        return sb.append(s, pos, n).toString();
    }

    public static StringBuffer escape(StringBuffer s) {

        int n = s.length();
        StringBuffer sb = new StringBuffer(n + 16);
        int pos = 0;
        for (int i = 0; i < n; ++i) {
            char ch = s.charAt(i);
            String repl = null;

            switch (ch) {
                case '"':   repl = "&#34;";  break;
                case '#':   repl = "&#35;";  break;
                case '$':   repl = "&#36;";  break;
                case '%':   repl = "&#37;";  break;
                case '&':   repl = "&#38;";  break;
                case '\'':  repl = "&#39;";  break;
                case '(':   repl = "&#40;";  break;
                case ')':   repl = "&#41;";  break;
                case '*':   repl = "&#42;";  break;
                case '+':   repl = "&#43;";  break;
                case '-':   repl = "&#45;";  break;
                //case '/':   repl = "&#47;";  break;
                case ';':   repl = "&#59;";  break;
                case '<':   repl = "&#60;";  break;
                //case '=':   repl = "&#61;";  break;
                case '>':   repl = "&#62;";  break;
                //case '@':   repl = "&#64;";  break;
                case '{':   repl = "&#123;"; break;
                case '|':   repl = "&#124;"; break;
                case '}':   repl = "&#125;"; break;

                default: break;
            }

            if (repl != null) {
                sb.append(s, pos, i).append(repl);
                pos = i + 1;
            }
        }
        return sb.append(s, pos, n);
    }
}