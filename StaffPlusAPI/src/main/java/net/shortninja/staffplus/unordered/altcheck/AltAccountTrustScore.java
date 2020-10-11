package net.shortninja.staffplus.unordered.altcheck;

public enum AltAccountTrustScore {

    POSITIVE(4),
    FAIRLY_POSITIVE(3),
    POSSIBLE(2),
    NOT_LIKELY(1);

    private final int score;

    AltAccountTrustScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public static AltAccountTrustScore fromScore(int score) {
        for (AltAccountTrustScore value : AltAccountTrustScore.values()) {
            if (value.score == score) {
                return value;
            }
        }
        throw new RuntimeException("AltAccountTrustScore does not support score value [" + score + "]");
    }
}
