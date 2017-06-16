package cn.payingcloud.util.id;

import java.util.UUID;

/**
 * @author YQ.Huang
 */
public interface IdGenerator {

    ClusterIdGenerator DEFAULT_CLUSTER_ID_GENERATOR = new ClusterIdGenerator(0);

    static String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    static long clusterId() {
        return DEFAULT_CLUSTER_ID_GENERATOR.nextId();
    }

    static String objectId() {
        return ObjectId.get().toString();
    }

}
