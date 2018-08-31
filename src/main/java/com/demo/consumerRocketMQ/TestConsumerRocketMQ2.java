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
 * rocketMQ消息队列消息消费者,订阅 testTopic 类型，tagB的消息
 *
 */
public class TestConsumerRocketMQ2 {
	
	public static void main(String[] args) {
		//多个消费者如果具有同样的组名，那么这些消费者必须只消费同一个topic
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("haha");
		consumer.setNamesrvAddr("127.0.0.1:9876");
		consumer.setMessageModel(MessageModel.BROADCASTING);
		consumer.setVipChannelEnabled(false);
		
		//订阅消息
		try {
			consumer.subscribe("testTopic", "tagA");
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					
					System.out.println("线程 " + Thread.currentThread().getName() + " Receive New Messages: " + new String(msgs.get(0).getBody()));
					
					//回执消息确认
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		
		try {
			consumer.start();
			System.out.println("TestConsumerRocketMQ2 Started.");
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
	
}
