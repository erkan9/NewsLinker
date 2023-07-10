package erkamber.enums;

public enum VoteTypeComment {
    COMMENT("comment");

    private final String type;

    VoteTypeComment(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
