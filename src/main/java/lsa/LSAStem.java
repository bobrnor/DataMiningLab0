package lsa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
 */
public class LSAStem {
    private static final Pattern kNamePattern =  Pattern.compile("([^\\|,=\\?]+)\\?*=");
    private static final Pattern kPartOfSpeechPattern =  Pattern.compile("(A|ADV|ADVPRO|ANUM|APRO|COM|CONJ|INTJ|NUM|PART|PR|S|SPRO|V)");
    private static final Pattern kSexPattern =  Pattern.compile("(жен|муж|сред)");

    private String m_name = null;
    private String m_partOfSpeech = null;
    private String m_sex = null;

    public LSAStem(final String description) {
        System.out.println("Create stem from description: " + description);

        if (description.length() > 0) {
            String[] vars = description.split("\\|");
            String dsc = vars[0];

            Matcher matcher = kNamePattern.matcher(dsc);
            if (matcher.find()) {
                m_name = matcher.group(1);
            }

            matcher = kPartOfSpeechPattern.matcher(dsc);
            if (matcher.find()) {
                m_partOfSpeech = matcher.group(1);
            }

            matcher = kSexPattern.matcher(dsc);
            if (matcher.find()) {
                m_sex = matcher.group(1);
            }
        }
    }

    boolean isValid() {
        return m_name != null && m_partOfSpeech != null;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String m_name) {
        m_name = m_name;
    }

    public String getPartOfSpeech() {
        return m_partOfSpeech;
    }

    public void setPartOfSpeech(String m_partOfSpeech) {
        m_partOfSpeech = m_partOfSpeech;
    }

    public String getSex() {
        return m_sex;
    }

    public void setSex(String m_sex) {
        m_sex = m_sex;
    }
}
