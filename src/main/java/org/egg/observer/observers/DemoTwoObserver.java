package org.egg.observer.observers;

import org.egg.observer.Observer;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/8 21:57
 */
public class DemoTwoObserver implements Observer {
    @Override
    public void update() {

    }

    @Override
    public void update( Object obj) {
        System.out.println("demoTwoObserver execute");
    }
}
