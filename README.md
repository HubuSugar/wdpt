# wdpt
spring boot快速搭建项目
maven管理
spring/mybatis框架
mysql数据库，redis缓存
thymeleaf模板引擎
solr全文搜索服务器


项目的主要功能：
1.登录注册：添加一个用户，涉及的点MD5对密码加密，其中密码加盐
           登录成功时会在客户端的浏览器下发一个随机的LoginTicket,这个ticket包含了用户信息userId
2.问题发布、评论：用字典树（前缀树）实现敏感词的过滤
3.对评论点赞、点踩：用redis实现set数据结构实现
4.关注用户、关注问题
5.新鲜事（推拉模式）
6.配置solr搜索服务器使用IKanalyzer中文分词
7.redis用作阻塞队列的生产者和消费者模式的异步队列
