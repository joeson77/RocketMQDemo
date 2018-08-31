RocketMQ常用命令解释:
一、DefaultMQPushConsumer
	1 String consumerGroup 消费者组名，必须设置 
		需要注意的是，多个消费者如果具有同样的组名，那么这些消费者必须只消费同一个topic，具体原因参见rocketmq问题汇总-一个consumerGroup只对应一个topic 
	2 MessageModel messageModel 
		消费的方式，分为两种： 
		2.1 BROADCASTING 广播模式，即所有的消费者可以消费同样的消息 
		2.2 CLUSTERING 集群模式，即所有的消费者平均来消费一组消息 
	3 ConsumeFromWhere consumeFromWhere 
		消费者从那个位置消费，分别为： 
		3.1 CONSUME_FROM_LAST_OFFSET：第一次启动从队列最后位置消费，后续再启动接着上次消费的进度开始消费 
		3.2 CONSUME_FROM_FIRST_OFFSET：第一次启动从队列初始位置消费，后续再启动接着上次消费的进度开始消费 
		3.3 CONSUME_FROM_TIMESTAMP：第一次启动从指定时间点位置消费，后续再启动接着上次消费的进度开始消费 
			以上所说的第一次启动是指从来没有消费过的消费者，如果该消费者消费过，那么会在broker端记录该消费者的消费位置，如果该消费者挂了再启动，那么自动从上次消费的进度开始，见RemoteBrokerOffsetStore 
	4 AllocateMessageQueueStrategy allocateMessageQueueStrategy 
		消息分配策略，用于集群模式下，消息平均分配给所有客户端 
		默认实现为AllocateMessageQueueAveragely 
	5 Map<topic, sub expression> subscription // topic对应的订阅tag 
	6 MessageListener messageListener //客户端消费消息的实现类 
	7 OffsetStore offsetStore //offset存储实现，分为本地存储或远程存储 
	8 int consumeThreadMin = 20 //线程池自动调整，参见MQClientInstance调整客户端消费线程池 
	9 int consumeThreadMax = 64//线程池自动调整，参见MQClientInstance调整客户端消费线程池 
	10 long adjustThreadPoolNumsThreshold = 100000 
	11 int consumeConcurrentlyMaxSpan = 2000//单队列并行消费最大跨度，用于流控 
	12 int pullThresholdForQueue = 1000 // 一个queue最大消费的消息个数，用于流控 
	13 long pullInterval = 0 //消息拉取时间间隔，默认为0，即拉完一次立马拉第二次，单位毫秒 
	14 consumeMessageBatchMaxSize = 1//并发消费时，一次消费消息的数量，默认为1，假如修改为50，此时若有100条消息，那么会创建两个线程，每个线程分配50条消息。 
	15 pullBatchSize = 32 //消息拉取一次的数量 
	16 boolean postSubscriptionWhenPull = false 
	17 boolean unitMode = false 
	18 DefaultMQPushConsumerImpl defaultMQPushConsumerImpl 
		消费者实现类，所有的功能都委托给DefaultMQPushConsumerImpl来实现
  重要方法 
	1 subscribe(String topic, String subExpression) 
	订阅某个topic，subExpression传*为订阅该topic所有消息 
	2 registerMessageListener(MessageListenerConcurrently messageListener) 
	注册消息回调，如果需要顺序消费，需要注册MessageListenerOrderly的实现 
	3 start 