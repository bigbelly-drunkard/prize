package org.egg.quartz;

import org.egg.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
public class WxOldUserInfoJobTest extends BaseTest {
    @Autowired
    private WxOldUserInfoJob wxOldUserInfoJob;

    @Test
    public void exe()  {
        wxOldUserInfoJob.exe();
    }

}