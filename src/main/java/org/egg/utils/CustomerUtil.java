package org.egg.utils;

import org.egg.model.DO.Customer;

/**
 * @author dataochen
 * @Description
 * @date: 2020/7/31 17:05
 */
public class CustomerUtil {
    private static ThreadLocal<Customer> customerThreadLocal = new ThreadLocal<>();

    public static void addCustomer(Customer customer) {
        customerThreadLocal.set(customer);
    }

    public static Customer getCustomer() {
        return customerThreadLocal.get();
    }
}
