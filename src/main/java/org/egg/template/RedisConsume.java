package org.egg.template;

/**
 * @author dataochen
 * @Description
 * @date: 2020/3/3 14:28
 */
@FunctionalInterface
public interface RedisConsume {
    public void consume(Object o);
}
