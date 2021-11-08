依赖common模块
实现多数据源注入和mybatis集成.
mybatis集成可以使用@MapperScan（直接扫描包）或者单个@Mapper
mybatis.mapper-locations 指定mapper.xml位置

一、datasource package 是基于AOP代理，
1.服务启动时读取所有默认和其他数据源，设置默认数据源和所有数据源，定义datasource,写入dbName和index的映射
2.在使用注解时，通过注解的index 获取对应的数据源

二、datasource2 package 是使用springboot方式，用于读写分离，查询方法(get，select，find,query开头)使用slave库，其他的使用master库
1.服务启动时注入默认和其他数据源
2.通过AOP定义DAO层方法调用时，根据执行的方法名，确定使用哪个数据源
