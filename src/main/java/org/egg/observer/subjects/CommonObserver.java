package org.egg.observer.subjects;

import org.egg.observer.Observable;
import org.egg.observer.Observer;

import java.util.List;
import java.util.Vector;

/**
 * @author cdt
 * @Description 逾期单观察者执行者
 * @date: 2018/3/7 21:14
 */
public class CommonObserver implements Observable {
    public CommonObserver(List<Observer> obLs) {
        super();
        obs.addAll(obLs);
    }

    //    vector 保证数据及时可见性
    private Vector<Observer> obs=new Vector<>();
    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObserver(Object obj) {
        obs.forEach(observer->observer.update(obj));
    }

    @Override
    public void notifyObserver() {
        obs.forEach(observer->observer.update());

    }
}
