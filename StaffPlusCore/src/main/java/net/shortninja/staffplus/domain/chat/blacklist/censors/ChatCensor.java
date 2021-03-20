package net.shortninja.staffplus.domain.chat.blacklist.censors;

public interface ChatCensor {

    String censor(String message);

    default String censor(String message, String word, String censorCharacter) {
        String stars = word.replaceAll(".", censorCharacter);
        return message.replace(word, stars);
    }
}
