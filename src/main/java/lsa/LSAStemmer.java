package lsa;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: bobrnor
 * Date: 12/8/13
 * Time: 20:13
 * To change this template use File | Settings | File Templates.
 */
public class LSAStemmer {
    private static final String[] kStemExecString = {"src/main/resources/mystem", "-nli", "-e utf-8"};
    private Process m_stemProcess = null;
    private BufferedReader m_stemReader = null;
    private BufferedWriter m_stemWriter = null;

    public LSAStemmer() {
        try {
            ProcessBuilder builder = new ProcessBuilder(kStemExecString);
            builder.redirectErrorStream(true);
            m_stemProcess = builder.start();
            m_stemReader = new BufferedReader(new InputStreamReader(m_stemProcess.getInputStream()));
            m_stemWriter = new BufferedWriter(new OutputStreamWriter(m_stemProcess.getOutputStream()));

//            System.out.println("Stem in: тест");
//            m_stemWriter.write("тест");
//            m_stemWriter.newLine();
//            m_stemWriter.flush();
//            String inline = m_stemReader.readLine();
//            System.out.println("Stem out: " + inline);

//            stem("проверка того, как работает стеммер из java");
        }
        catch (IOException e) {
            e.printStackTrace();

            m_stemProcess = null;
            m_stemReader = null;
            m_stemWriter = null;
        }
    }

    public boolean isReady() {
        return m_stemProcess != null && m_stemReader != null && m_stemWriter != null;
    }

    public List<LSAStem> stem(final String line) {
        ArrayList<LSAStem> result = new ArrayList<LSAStem>();

        if (line == null || line.length() == 0) {
            return result;
        }

        try {
            m_stemWriter.write(line);
            m_stemWriter.newLine();
            m_stemWriter.flush();

            int wordCount = line.split(" +").length;
            System.out.println("(" + wordCount + ") " + line);

            while (wordCount > 0) {
                wordCount--;
                String inline = m_stemReader.readLine();
                LSAStem stem = new LSAStem(inline);
                if (stem.isValid()) {
                    result.add(stem);
                }
            }
        }
        catch (Exception e) {
            result.clear();

            e.printStackTrace();
        }
        return result;
    }
}
