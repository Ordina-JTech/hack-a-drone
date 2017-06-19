# Hack a Drone presentation

Test this page using

```
docker build -t hackadrone-ws . && \
docker run --name hackadrone-ws-site -d -p 9080:80 hackadrone-ws && \
open http://localhost:9080
```

And kill it using
```
docker rm -f hackadrone-ws-site && \
docker rmi -f hackadrone-ws
```
