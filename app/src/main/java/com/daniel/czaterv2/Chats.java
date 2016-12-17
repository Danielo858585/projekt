package com.daniel.czaterv2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 16.12.2016.
 */
public class Chats {
    private List<CzatListResponseDetails> chats = new ArrayList<>();


    public List<CzatListResponseDetails> getChats() {
        return chats;
    }

    public void setChats(List<CzatListResponseDetails> chats) {
        this.chats = chats;
    }

    public Chats(List<CzatListResponseDetails> chats) {

        this.chats = chats;
    }
}

