package com.github.webproxy.core.conf;

import com.github.monkeywie.proxyee.proxy.ProxyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "proxy")
public class ProxyConf {

    //代理项
    private List<Item> items = new ArrayList<Item>() {{
        add(Item.builder().port(808).build());
    }};

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        //端口
        private int port;

        //权限
        private List<Auth> auths;

        //二级代理
        private PreProxy proxy;

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Auth {
        //代理的账号
        private String userName;

        //代理的密码
        private String passWord;

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreProxy {

        private ProxyType proxyType;

        private String host;

        private int port;

        private String user;

        private String pwd;

    }


}
