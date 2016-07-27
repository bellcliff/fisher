package org.free;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by byang1 on 7/26/16.
 */
public class Buffer {
    int key;
    long interval;
    long startTime;

    enum FishBuffer {
        Bait,
        Knife
    }

    Buffer(int key, long interval) {
        this.key = key;
        this.interval = interval;
        this.startTime = System.currentTimeMillis();
    }

    void check() {
        if (System.currentTimeMillis() - startTime > interval) {
            MyAction.keyPress(this.key);
            this.startTime = System.currentTimeMillis();
        }
    }

    static Buffer startBuffer(FishBuffer buffer){
        switch (buffer){
            case Bait:
                return new Bait();
            case Knife:
                return new Knife();
        }
        return null;
    }

    static void checkBuffer(){

    }

    static class BufferManagement{
        List<Buffer> buffers = new ArrayList<>();
        BufferManagement(FishBuffer... buffers){
            for (FishBuffer buffer: buffers)
            this.buffers.add(Buffer.startBuffer(buffer));
        }

        void check(){
            buffers.forEach(Buffer::check);
        }
    }

    static class Bait extends Buffer {

        private Bait() {
            super(KeyEvent.VK_X, 3000 + 10 * 60 * 1000);
        }
    }

    static class Knife extends Buffer {
        private Knife() {
            super(KeyEvent.VK_F, 3000 + 60 * 60 * 1000);
        }
    }
}
