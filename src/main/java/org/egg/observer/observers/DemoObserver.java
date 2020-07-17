package org.egg.observer.observers;

import org.egg.observer.Observer;

/**
 * @author cdt
 * @Description
 * @date: 2017/11/8 21:55
 */
public class DemoObserver implements Observer {
    @Override
    public void update( Object obj) {
        System.out.println("demoObserver execute");
    }

    @Override
    public void update() {

    }
}
