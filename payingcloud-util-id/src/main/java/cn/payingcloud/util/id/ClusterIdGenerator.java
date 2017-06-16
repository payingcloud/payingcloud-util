package cn.payingcloud.util.id;

/**
 * An time sequential and unique global ID generator.
 * Based on https://github.com/twitter/snowflake
 */
public class ClusterIdGenerator {

    private long workerId;
    private long sequence = 0L;

    private static long twepoch = 1288834974657L;

    private static long workerIdBits = 10L;
    private static long maxWorkerId = ~(-1L << workerIdBits);
    private static long sequenceBits = 12L;

    private static long workerIdShift = sequenceBits;
    private static long timestampLeftShift = sequenceBits + workerIdBits;
    private static long sequenceMask = ~(-1L << sequenceBits);

    private long lastTimestamp = -1L;

    public ClusterIdGenerator(long workerId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * Get timestamp from the generated id
     *
     * @param id generated id
     * @return UNIX timestamp from midnight, January 1, 1970 UTC
     */
    public static long timestamp(long id) {
        return (id >> timestampLeftShift) + twepoch;
    }
}
