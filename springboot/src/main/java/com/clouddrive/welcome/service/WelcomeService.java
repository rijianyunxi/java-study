package com.clouddrive.welcome.service;

import com.clouddrive.welcome.dto.WelcomeData;
import org.springframework.stereotype.Service;

/** 业务层：目前只创建问候数据，后续可在这里扩展业务逻辑。 */
@Service
public class WelcomeService {

    public WelcomeData getWelcomeData() {
        return new WelcomeData("hello world");
    }
}
