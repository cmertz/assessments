import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Anagrams {

    private static String normalizeWord(final String word) {
        char[] chars = word.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    private static void addWord(final Map<String, Set<String>> anagrams,
            final String word) {

        final String key = normalizeWord(word);

        Set<String> wordList = anagrams.get(key);
        if (wordList == null) {
            wordList = new HashSet<String>();
            anagrams.put(key, wordList);
        }

        if (wordList.contains(word)) {
            return;
        }

        wordList.add(word.toLowerCase());
    }

    private static List<String> readFile(final String fileName)
            throws IOException {

        final FileReader fileIn = new FileReader(fileName);
        final BufferedReader in = new BufferedReader(fileIn);

        final List<String> lines = new LinkedList<String>();

        for (String line; (line = in.readLine()) != null;) {
            lines.add(line);
        }

        in.close();
        fileIn.close();

        return lines;
    }

    private static String setToString(final Set<String> set) {

        final StringBuffer sb = new StringBuffer();

        sb.append('[');

        final String[] array = set.toArray(new String[set.size()]);

        for (int i = 0; i < array.length - 1; i++) {
            sb.append(array[i]);
            sb.append(',');
        }

        sb.append(array[array.length - 1]);

        sb.append(']');

        return sb.toString();
    }

    public static void main(String... args) throws IOException {

        final Map<String, Set<String>> anagrams = new HashMap<String, Set<String>>();

        for (String word : readFile(args[0])) {
            addWord(anagrams, word);
        }

        int longest = 0;
        int most = 0;

        String longestKey = "";
        String mostKey = "";

        for (String key : anagrams.keySet()) {

            final Set<String> list = anagrams.get(key);

            if (list.size() == 1) {
                continue;
            }

            if (key.length() > longest) {
                longestKey = key;
                longest = key.length();
            }

            if (list.size() > most) {
                mostKey = key;
                most = list.size();
            }
        }

        System.out.println("longest " +
                           setToString(anagrams.get(mostKey)) +
			   "\nmost " +
                           setToString(anagrams.get(longestKey)));
    }
}
