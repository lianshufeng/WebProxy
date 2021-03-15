# WebProxy

- Run
````shell
java -jar webproxy-0.0.1-SNAPSHOT.jar
````

- application.yml
````shell
proxy:
  items:
    - port: 808
      proxy:
        proxyType: SOCKS5
        host: 127.0.0.1
        port: 1080

````
