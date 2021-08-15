# 技术栈

**SpringBoot：2.0.8.RELEASE**

**MySql：5.7**

**Redis：3.2.1**

利用Redis实现分布式锁，实现防止同一用户红包重复抢红包

# Redis的使用

1. 在发红包业务模块中，我们将红包个数和每个红包的随机金额存入redis缓存中。
2. 在"点红包"的业务逻辑中，是去缓存中判断红包个数是否大于0。
3. 在"拆红包"的业务逻辑中，也是从缓存的红包随机金额队列中去读取红包金额。
4. 同时在优化的时候将借助Redis单线程特性与操作的原子性实现抢红包的锁操作。

可见，Redis在抢红包系统中占据很重要的位置。一方面Redis将大大减少高并发情况下频繁查询数据库的操作，从而减轻数据库的压力；另一方面，Redis将提高系统的整体响应性能和保证数据的一致性。



# 算法

- 对于发红包的ID采用分布式ID生成算法雪花算法
- 红包的金额生成采用二倍均值算法



# 项目心路

1. 利用Redis在未实现分布式锁的情况下，会发生同一个用户重复抢红包的现象（利用JMeter测试），解决方法引入分布式锁，实现同一用户只能抢一次

2. 分布式锁引入后，使用`expire`，设置锁超时参数防止宕机锁无法释放

3. Redis的存储选用String和List，String存储当前红包剩余数量，List存储红包随机金额，用户点开红包即弹出一个数据作为红包数据，但是在多线程环境下，会发现即使红包实际数量为空了，但Redis存储的红包剩余数量依旧是大于0的，这是因为对红包数量的操作不是原子性导致的：

```java
//红包个数减一
String redTotalKey = redId+RedRobConstant.TOTAL;
//Integer currTotal=valueOperations.get(redTotalKey)!=null? (Integer) valueOperations.get(redTotalKey) : 0;
//这里不是-1 不是原子性操作，所以会发生在多线程环境下，即使红包抢完了，红包总数并不是0
valueOperations.increment(redTotalKey, -1);
//valueOperations.set(redTotalKey,currTotal-1);
log.info("剩余红包数："+(valueOperations.get(redTotalKey)));
```

解决方法就是调用Redix的`incrby key increment`实现-1操作的原子性

3. List的长度不就是剩余红包数量，实时获取不就得了，为什么还要用String去存剩余红包数量？
4. 最开始红包的ID生成策略采用的是当前时间戳，但是可能会发生Key重复现象，于是改用雪花算法生成ID，生成的ID也是递增的，满足Innodb的查询特点。



# 项目缺点和解决方案

1. Redis未作集群，单机Redis很可能在并发高的时候宕机

2. 采用较简单的自定义redis分布式锁，为避免死锁定义默认过期时间，但可能会发生业务未执行完，就自动释放锁的问题，所以这也是这时候就需要一把可重入锁——Redisson

3. 回写数据库应该采用MQ异步去写入（虽然开启了@EnableAsync），但用MQ是更好的策略

4. 多次与redis交互，消耗了很多时间（交互一次大概是几十到上百毫秒），分布式锁本身也需要和redis交互，可以采用lua表达式来达到缩减redis交互次数以及保证高并发情况下与redis多个交互命令的原子性。

   线程去获取锁，获取成功: 执行lua脚本，保存数据到redis数据库。

   线程去获取锁，获取失败: 一直通过while循环尝试获取锁，获取成功后，执行lua脚本，保存数据到redis数据库。

   主要是如果你的业务逻辑复杂的话，通过封装在lua脚本中发送给redis，而且redis是单线程的，这样就保证这段复杂业务逻辑执行的**原子性**。

