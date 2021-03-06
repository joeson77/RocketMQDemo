package com.demo.consumerRocketMQ;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * 
 *                                        ,s555SB@@&                         
 *                                      :9H####@@@@@Xi                       
 *                                     1@@@@@@@@@@@@@@8                      
 *                                   ,8@@@@@@@@@B@@@@@@8                     
 *                                  :B@@@@X3hi8Bs;B@@@@@Ah,                  
 *             ,8i                  r@@@B:     1S ,M@@@@@@#8;                
 *            1AB35.i:               X@@8 .   SGhr ,A@@@@@@@@S               
 *            1@h31MX8                18Hhh3i .i3r ,A@@@@@@@@@5              
 *            ;@&i,58r5                 rGSS:     :B@@@@@@@@@@A              
 *             1#i  . 9i                 hX.  .: .5@@@@@@@@@@@1              
 *              sG1,  ,G53s.              9#Xi;hS5 3B@@@@@@@B1               
 *               .h8h.,A@@@MXSs,           #@H1:    3ssSSX@1                 
 *               s ,@@@@@@@@@@@@Xhi,       r#@@X1s9M8    .GA981              
 *               ,. rS8H#@@@@@@@@@@#HG51;.  .h31i;9@r    .8@@@@BS;i;         
 *                .19AXXXAB@@@@@@@@@@@@@@#MHXG893hrX#XGGXM@@@@@@@@@@MS       
 *                s@@MM@@@hsX#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&,     
 *              :GB@#3G@@Brs ,1GM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@B,    
 *            .hM@@@#@@#MX 51  r;iSGAM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@8    
 *          :3B@@@@@@@@@@@&9@h :Gs   .;sSXH@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@:   
 *      s&HA#@@@@@@@@@@@@@@M89A;.8S.       ,r3@@@@@@@@@@@@@@@@@@@@@@@@@@@r   
 *   ,13B@@@@@@@@@@@@@@@@@@@5 5B3 ;.         ;@@@@@@@@@@@@@@@@@@@@@@@@@@@i   
 *  5#@@#&@@@@@@@@@@@@@@@@@@9  .39:          ;@@@@@@@@@@@@@@@@@@@@@@@@@@@;   
 *  9@@@X:MM@@@@@@@@@@@@@@@#;    ;31.         H@@@@@@@@@@@@@@@@@@@@@@@@@@:   
 *   SH#@B9.rM@@@@@@@@@@@@@B       :.         3@@@@@@@@@@@@@@@@@@@@@@@@@@5   
 *     ,:.   9@@@@@@@@@@@#HB5                 .M@@@@@@@@@@@@@@@@@@@@@@@@@B   
 *           ,ssirhSM@&1;i19911i,.             s@@@@@@@@@@@@@@@@@@@@@@@@@@S  
 *              ,,,rHAri1h1rh&@#353Sh:          8@@@@@@@@@@@@@@@@@@@@@@@@@#: 
 *            .A3hH@#5S553&@@#h   i:i9S          #@@@@@@@@@@@@@@@@@@@@@@@@@A.
 *
 *
 * @author 厉昀键
 * create in 2018年8月31日
 * rocketMQ消息队列消息消费者,订阅 testTopic 类型，tagA或tagB的消息
 *
 */
public class TestConsumerRocketMQ1 {
	
	public static void main(String[] args) throws InterruptedException, MQClientException {
		
		//多个消费者如果具有同样的组名，那么这些消费者必须只消费同一个topic
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("haha");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        
        /**
         * 消费者默认是集群的方式消费的 消费者还可以用广播的模式进行消费
         * 
         * 设置广播模式
         * consumer.setMessageModel(MessageModel.BROADCASTING);
         */
        
        //consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setVipChannelEnabled(false);
        
        /**
         * 若以集群模式订阅某一主题的消息，会拿到该消息，进行tag标签筛选，同时订阅同主题的服务器将拿不到该条消息
         */
        //订阅testTopic主题下，标签为tagA或者tagB的消息
        consumer.subscribe("testTopic", "tagA || tagB");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                if (msg.getTags().equals("tagA")) {
                		// 执行tagA的消费
                    String message = new String(msg.getBody());
                    System.out.println(Thread.currentThread().getName() + " Receive tagA Messages: " +message);
                }else if(msg.getTags().equals("tagB")){
                		// 执行tagB的消费
                    String message = new String(msg.getBody());
                    System.out.println(Thread.currentThread().getName() + " Receive tagB Messages: "+message);
				}
                //回执消息确认
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("Consumer Started.");
    }
	
}
