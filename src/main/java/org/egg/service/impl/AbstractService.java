package org.egg.service.impl;

import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;

public abstract class AbstractService<T> {

    public void update(T record) {
        int i = updateService(record);
        if (i == 0) {
            throw new CommonException(CommonErrorEnum.DB_UPDATE_VERSION_EXCEPTION);
        }

    }

    abstract int updateService(T record);
}
