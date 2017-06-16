package cn.payingcloud.util.id;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author YQ.Huang
 */
public class IdGeneratorTest {
    @Test
    public void objectId() throws Exception {
        System.out.println(IdGenerator.objectId());
    }

    @Test
    public void uuid() throws Exception {
        String uuid = IdGenerator.uuid();
        System.out.println(uuid);
        assertEquals(32, uuid.length());
    }

    @Test
    public void sequenceId() throws Exception {
        long clusterId = IdGenerator.clusterId();
        System.out.println(IdGenerator.clusterId());
        assertEquals(18, String.valueOf(clusterId).length());
    }

}