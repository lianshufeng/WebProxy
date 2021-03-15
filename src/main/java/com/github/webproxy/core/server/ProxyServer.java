package com.github.webproxy.core.server;

import com.github.monkeywie.proxyee.proxy.ProxyConfig;
import com.github.monkeywie.proxyee.server.HttpProxyServer;
import com.github.monkeywie.proxyee.server.HttpProxyServerConfig;
import com.github.monkeywie.proxyee.server.auth.BasicHttpProxyAuthenticationProvider;
import com.github.webproxy.core.conf.ProxyConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProxyServer {

    @Autowired
    private ProxyConf proxyConf;

    private Vector<HttpProxyServer> servers = new Vector<>();


    @Autowired
    private void init(ApplicationContext applicationContext) {
        servers.addAll(proxyConf.getItems().parallelStream().map((it) -> {
            HttpProxyServer server = toHttpProxyServer(it);
            new Thread(() -> {
                log.info("[proxy]  -> [{}]", it.getPort());
                server.start(it.getPort());
            }).start();
            return server;
        }).collect(Collectors.toSet()));


    }

    @Autowired
    private void shutdown(ApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            servers.parallelStream().forEach((it) -> {
                it.close();
            });
            log.info("[proxy] -> {} ", "stop");
        }));
    }


    /**
     * 转换到服务器
     *
     * @param item
     * @return
     */
    private HttpProxyServer toHttpProxyServer(ProxyConf.Item item) {
        HttpProxyServer server = new HttpProxyServer();


        HttpProxyServerConfig config = new HttpProxyServerConfig();


        //权限
        Optional.ofNullable(item.getAuths()).ifPresent((auths) -> {
            config.setAuthenticationProvider(new BasicHttpProxyAuthenticationProvider() {
                @Override
                protected boolean authenticate(String usr, String pwd) {
                    for (ProxyConf.Auth auth : auths) {
                        if (usr.equals(auth.getUserName()) && pwd.equals(auth.getPassWord())) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        });

        //二级代理
        Optional.ofNullable(item.getProxy()).ifPresent((it) -> {
            ProxyConfig proxyConfig = new ProxyConfig();
            BeanUtils.copyProperties(it, proxyConfig);
            server.proxyConfig(proxyConfig);
        });

        server.serverConfig(config);
        return server;
    }


}
