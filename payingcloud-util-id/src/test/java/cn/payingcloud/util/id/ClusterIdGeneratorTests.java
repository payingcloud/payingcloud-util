package cn.payingcloud.util.id;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClusterIdGeneratorTests {

    /**
     * Verify if performance is good
     */
    @Test(timeout = 1000)
    public void goodPerformance() {
        ClusterIdGenerator idGenerator = new ClusterIdGenerator(0);
        for (int i = 0; i < 10000; i++) {
            idGenerator.nextId();
        }
    }

    /**
     * Verify if unique
     */
    @Test
    public void unique() {
        Set<Long> ids = new HashSet<>();
        ClusterIdGenerator idGenerator = new ClusterIdGenerator(0);
        for (int i = 0; i < 1000000; i++) {
            assertTrue("Duplicate found!", ids.add(idGenerator.nextId()));
        }
    }

    /**
     * Verify if time-sequence
     */
    @Test
    public void sequential() {
        ClusterIdGenerator idGenerator = new ClusterIdGenerator(0);
        long current = Long.MIN_VALUE;
        for (int i = 0; i < 100; i++) {
            long next = idGenerator.nextId();
            if (current < next) {
                current = next;
            } else {
                fail("Not sequential!");
            }
        }
    }

    /**
     * Verify if works(unique) in distribution
     */
    @Test
    public void distributed() {
        Set<Long> ids = new HashSet<>();
        for (int j = 0; j < 3; j++) {
            ClusterIdGenerator idGenerator = new ClusterIdGenerator(j);
            for (int i = 0; i < 1000000; i++) {
                assertTrue("Bad thing happened!", ids.add(idGenerator.nextId()));
            }
        }
    }

    @Test
    public void print() {
        ClusterIdGenerator idGenerator = new ClusterIdGenerator(0);
        System.out.println("Here are generated IDs:");
        for (int i = 0; i < 100; i++) {
            System.out.println(idGenerator.nextId());
        }
        System.out.println("Over");
    }

    @Test
    public void timestamp() {
        ClusterIdGenerator idGenerator = new ClusterIdGenerator(0);
        ClusterIdGenerator.timestamp(idGenerator.nextId());
        System.out.println("expected: " + System.currentTimeMillis());
        System.out.println("actual: " + ClusterIdGenerator.timestamp(idGenerator.nextId()));
        assertTrue(System.currentTimeMillis() - ClusterIdGenerator.timestamp(idGenerator.nextId()) <= 100);
        assertTrue(System.currentTimeMillis() - ClusterIdGenerator.timestamp(idGenerator.nextId()) >= 0);
    }
}
