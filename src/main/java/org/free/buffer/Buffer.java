package org.free.buffer;

import org.free.MyAction;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Buffer {
    private int key;
    private long interval;
    private long startTime;

    public enum FishBuffer {
        Bait,
        Knife,
        Special
    }

    private Buffer(int key, long interval) {
        this.key = key;
        this.interval = interval;
        this.startTime = System.currentTimeMillis();
    }

    private void check() {
        long gap = System.currentTimeMillis() - startTime;
        if (gap > interval) {
            MyAction.keyPress(this.key);
            this.startTime = System.currentTimeMillis();
        }
    }

    private static Buffer startBuffer(FishBuffer buffer){
        switch (buffer){
            case Bait:
                return new Bait();
            case Knife:
                return new Knife();
            case Special:
                return new Special();
        }
        return null;
    }

    public static class BufferManagement{
        List<Buffer> buffers = new ArrayList<>();
        public BufferManagement(FishBuffer... buffers){
            for (FishBuffer buffer: buffers)
            this.buffers.add(Buffer.startBuffer(buffer));
        }

        public void check(){
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

    static class Special extends Buffer {

        private Special() {
            super(KeyEvent.VK_G, 3000 + 3 * 60 * 1000);
        }
    }
}
