package lsa;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class LSAStemmer {
    private static final String kStemExecString = "/resources/mystem -nli -e utf-8";
    private Process m_stemProcess = null;
    private BufferedReader m_stemReader = null;
    private BufferedWriter m_stemWriter = null;

    public LSAStemmer() {
//        m_stemProcess = Runtime.getRuntime().exec(kStemExecString);
//        m_stemReader = new BufferedReader(new InputStreamReader(m_stemProcess.getInputStream()));
//        m_stemWriter = new BufferedWriter(new OutputStreamWriter(m_stemProcess.getOutputStream()));
    }


}
