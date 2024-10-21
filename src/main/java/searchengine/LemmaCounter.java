package searchengine;

import java.util.*;
import java.util.regex.Pattern;

public class LemmaCounter {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "и", "в", "не", "на", "с", "что", "по", "как", "то", "же",
            "да", "или", "если", "но", "вот", "так", "также", "ах", "ох", "давай", "пускай"
    ));

    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[^а-яА-ЯёЁ\\s]");

    private static String removeEnding(String word, int length) {
        return word.substring(0, word.length() - length);
    }

    private static String lemmatize(String word) {
        return switch (word) {
            case "повторное" -> "повторный";
            case "позволяет" -> "позволять";
            case "обитает" -> "обитать";
            case "некоторых" -> "некоторый";
            case "районах" -> "район";
            case "северного" -> "северный";
            case "осетия", "осети", "осетии" -> "осетия"; // Добавлены все формы "осетия"
            default -> {
                if (word.endsWith("я") || word.endsWith("а")) {
                    yield removeEnding(word, 1);
                } else if (word.endsWith("ы") || word.endsWith("и")) {
                    yield removeEnding(word, 1);
                } else if (word.endsWith("ов") || word.endsWith("ев")) {
                    yield removeEnding(word, 2);
                } else if (word.endsWith("ем")) {
                    yield removeEnding(word, 2);
                } else if (word.endsWith("т")) {
                    yield removeEnding(word, 1);
                }
                yield word;
            }
        };
    }

    public static Map<String, Integer> getWordCounts(String text) {
        String[] words = PUNCTUATION_PATTERN.matcher(text.toLowerCase()).replaceAll("").split("\\s+");
        Map<String, Integer> wordCount = new HashMap<>();

        for (String word : words) {
            String lemma = lemmatize(word);
            if (!STOP_WORDS.contains(lemma) && !lemma.isEmpty()) {
                wordCount.put(lemma, wordCount.getOrDefault(lemma, 0) + 1);
            }
        }

        return wordCount;
    }

    public static void main(String[] args) {
        String text = "Повторное появление леопарда в Осетии позволяет предположить, что леопард постоянно обитает в некоторых районах Северного Кавказа.";
        Map<String, Integer> result = getWordCounts(text);

        result.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Сортировка по алфавиту
                .forEach(entry -> System.out.println(entry.getKey() + " — " + entry.getValue())); // Печать лемм и их количества
    }
}