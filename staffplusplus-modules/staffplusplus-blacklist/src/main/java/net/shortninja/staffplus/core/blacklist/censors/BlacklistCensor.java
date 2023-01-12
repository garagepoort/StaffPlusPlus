package net.shortninja.staffplus.core.blacklist.censors;

public interface BlacklistCensor {

    String censor(String message);

    default String censor(String message, String word, String censorCharacter) {
        String stars = word.replaceAll(".", censorCharacter);
        return message.replace(word, stars);
    }
}
