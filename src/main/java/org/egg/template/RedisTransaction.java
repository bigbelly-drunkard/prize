package org.egg.template;

/**
 * @author dataochen
 * @Description
 * @date: 2020/2/28 18:07
 */
@FunctionalInterface
public interface RedisTransaction {
    /**
     * 执行类
     */
    public void doAction();
}
