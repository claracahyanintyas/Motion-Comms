package org.individualproject.semester3project.infrastructure.media;

public enum Bucket {
    TRANSACTION_PROOFS("transaction-proofs"),
    CHAT_MEDIA("chat-media"),
    COMMISSION_MEDIA("commission-media");

    private final String name;

    Bucket(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
