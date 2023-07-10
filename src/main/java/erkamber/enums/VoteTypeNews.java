package erkamber.enums;

public enum VoteTypeNews {

    NEWS("news");

    private final String type;

    VoteTypeNews(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

