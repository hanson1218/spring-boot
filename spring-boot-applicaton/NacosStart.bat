::进入E盘
e:
::进入redis目录
cd E:\tools\nacos-server-2.0.3\bin
::启动服务
startup.cmd -m standalone



cd E:\tools\Sentinel-1.8.2
java -Dserver.port=8083 -Dcsp.sentinel.dashboard.server=localhost:8083 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.2.jar