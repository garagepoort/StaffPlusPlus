package net.shortninja.staffplus.server.chat;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.server.data.config.Options;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;


public class BlacklistFactory {
    private static String[] words = null;
    private static String[] domains = null;
    private static String[] periods = null;
    private Options options = StaffPlus.get().options;
    private String originalMessage;
    private String censoredMessage;
    private boolean hasChanged = false;

    public BlacklistFactory(String originalMessage) {
        if (words == null) {
            cleanArrays();
        }

        this.originalMessage = originalMessage;
        this.censoredMessage = originalMessage;
    }

    public boolean hasChanged() {
        return hasChanged;
    }

    public String getResult() {
        return censoredMessage;
    }

    public BlacklistFactory runCheck() {
        censoredMessage = checkIllegalCharacters();
        censoredMessage = checkIllegalWords();
        censoredMessage = checkDomains();

        return this;
    }

    private String checkIllegalCharacters() {
        String newMessage = originalMessage;

        for (String string : options.chatBlacklistCharacters) {
            if (newMessage.contains(string)) {
                newMessage = newMessage.replace(string, options.chatBlacklistCharacter);
                hasChanged = true;
            }
        }

        return newMessage;
    }

    private String checkIllegalWords() {
        String newMessage = censoredMessage;

        for (String word : newMessage.split(" ")) {
            if (isIn(words, word.toLowerCase()) && !isBypassable(word.toLowerCase())) {
                newMessage = censor(newMessage, word);
                hasChanged = true;
            }
        }
        return newMessage;
    }

    private String checkDomains() {
        String newMessage = censoredMessage;
        boolean hasPeriods = false;

        for (String word : newMessage.split(" ")) {
            if (containsPeriod(word)) {
                for (String period : periods) {
                    newMessage = newMessage.replace(period, ".");
                }

                hasPeriods = true;
            }
        }

        if (hasPeriods) {
            String[] words = newMessage.split(Pattern.quote("."));
            if (words.length >= 1) {
                String previousWord = words[0];

                for (int i = 0; i < words.length; i++) {
                    String word = words[i];

                    if (containsDomain(word)) {
                        newMessage = censor(newMessage, word);
                        newMessage = censor(newMessage, ".");

                        if (!previousWord.isEmpty()) {
                            newMessage = censor(newMessage, previousWord);
                        }

                        hasChanged = true;
                    }

                    previousWord = word;
                }
            }
        }
        return newMessage;
    }

    private String censor(String messsage, String word) {
        String censored = messsage;
        StringBuilder builder = new StringBuilder();

        for (int k = 0; k < word.length(); k++) {
            builder.append(options.chatBlacklistCharacter);
        }

        censored = censored.replace(word, builder.toString());

        return censored;
    }

    private boolean isIn(String[] array, String string) {
        return Arrays.binarySearch(array, string) >= 0;
    }

    private boolean isBypassable(String word) {
        boolean isBypassable = false;

        for (String string : options.chatBlacklistAllowed) {
            if (word.contains(string.toLowerCase())) {
                isBypassable = true;
                break;
            }
        }

        return isBypassable;
    }

    private boolean containsPeriod(String word) {
        boolean contains = false;

        for (String period : periods) {
            if (word.endsWith(period) || word.startsWith(period) || word.contains(period)) {
                contains = true;
                break;
            }
        }

        if (!contains) {
            if (word.endsWith(".") || word.startsWith(".") || word.contains(".")) {
                contains = true;
            }
        }

        return contains;
    }

    private boolean containsDomain(String word) {
        boolean contains = false;

        for (String domain : domains) {
            if (word.equalsIgnoreCase(domain)) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    private void cleanArrays() {
        //Collections.sort(options.chatBlacklistWords);
        //Collections.sort(options.chatBlacklistDomains);
        //Collections.sort(options.chatBlacklistPeriods);
        words = options.chatBlacklistWords.toArray(new String[options.chatBlacklistWords.size()]);
        domains = options.chatBlacklistDomains.toArray(new String[options.chatBlacklistDomains.size()]);
        periods = options.chatBlacklistPeriods.toArray(new String[options.chatBlacklistPeriods.size()]);
        Arrays.sort(words);
        sanitize(words);
        sanitize(domains);
        sanitize(periods);
    }

    private void sanitize(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].toLowerCase();
        }
    }
}