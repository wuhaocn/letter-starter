## 服务注册
```
PUT /v1/agent/service/register HTTP/1.1
Content-Type: application/json; charset=UTF-8
Accept: */*
User-Agent: Java/1.8.0_231
Host: 127.0.0.1:18500
Connection: keep-alive
Content-Length: 185

{"id":"test","name":"test","tags":["service"],"address":"192.168.1.6","port":8901,"meta":{"version":"1.0","item":"MSG","app":"MSG"},"enableTagOverride":false,"checks":[],"weights":null}HTTP/1.1 200 OK
Vary: Accept-Encoding
X-Consul-Default-Acl-Policy: allow
Date: Tue, 15 Nov 2022 15:43:38 GMT
Content-Length: 0
```

## 标签规则

## 动态标签
```
在通过Consul注册的时候可以携带自定义标签:

"Meta": {
       "app": "nginx",
       "project": "abc"

   },
这些自定义的标签,在Prometheus里会自动添加下面2个标签:
__meta_consul_service_metadata_app=nginx
__meta_consul_service_metadata_project=abc

- job_name: 'consul-prometheus'
    consul_sd_configs:
      - server: '172.16.83.201:8500'
        services: []
    relabel_configs:
      - regex: __meta_consul_service_metadata_(.+)
        action: labelmap

匹配__meta_consul_service_metadata_(.+)的标签,然后将匹配到的分组(.+) 生成一个新的标签.
也就是__meta_consul_service_metadata_app=nginx 匹配到app=nginx .
然后生产一个app=nginx的标签.也就是在Prometheus的UI界面中看到的target labels

```
