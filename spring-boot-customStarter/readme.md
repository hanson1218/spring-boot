自定义starter：

1.配置spring.factories 指定自动装配类
2.指定需要装配的配置文件对象
3.配置文件对象添加@ConfigurationProperties 指定前缀。 不要再加@Component注解，不然会失效