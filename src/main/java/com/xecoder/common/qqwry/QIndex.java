package com.xecoder.common.qqwry;

public class QIndex {
    private final long minIP;
    private final long maxIP;
    private final int recordOffset;

    public QIndex(final long minIP, final long maxIP, final int recordOffset) {
        this.minIP = minIP;
        this.maxIP = maxIP;
        this.recordOffset = recordOffset;
    }

    public long getMaxIP() {
        return maxIP;
    }

    public long getMinIP() {
        return minIP;
    }

    public int getRecordOffset() {
        return recordOffset;
    }

    @Override
    public String toString() {
        return "QIndex{" +
                "minIP=" + minIP +
                ", maxIP=" + maxIP +
                ", recordOffset=" + recordOffset +
                '}';
    }
}
