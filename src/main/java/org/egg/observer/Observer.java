package org.egg.observer;

/**
 * @author cdt
 * @Description 观察者模式
 * @date: 2017/11/8 21:54
 */
public interface Observer {
    /**
     *观察者执行方法
     */
    void update( Object obj);

    void update();
}
