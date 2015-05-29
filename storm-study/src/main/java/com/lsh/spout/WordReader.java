package com.lsh.spout;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordReader extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	
	private boolean completed = false;
	
	/**
	 * һ�� tuple ����ɹ������ø÷��� 
	 */
	public void ack(Object msgId) {
		System.out.println("OK:"+msgId);
	}
	public void close() {}
	
	/**
	 * һ��tuple ����ʧ�ܺ����ô˷���
	 */
	public void fail(Object msgId) {
		System.out.println("FAIL:"+msgId);
	}

	/**
	 * The only thing that the methods will do It is emit each 
	 * file line
	 */
//	public void nextTuple() {
//		/**
//		 * The nextuple it is called forever, so if we have been readed the file
//		 * we will wait and then return
//		 */
//		if(completed){
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				//Do nothing
//			}
//			return;
//		}
//		String str;
//		//Open the reader
//		BufferedReader reader = new BufferedReader(fileReader);
//		try{
//			//Read all lines
//			while((str = reader.readLine()) != null){
//				
//				/**
//				 * ���ｫ str������Ϊ msgId ���ݸ�id��׷�پ���tuple
//				 * 
//				 *  �����tuple�����͵���Ϣ������bolt��� 
//				 *  storm������ɴ������������tuple�������storm��⵽һ��tuple����ȫ�����ˣ�
//				 *  ��ôstorm�����ʼ���Ǹ�message-id��Ϊ����ȥ������ϢԴ��ack������
//				 *  ��֮storm�����spout��fail������
//				 *  ֵ��ע���һ���ǣ� storm����ack����fail��taskʼ���ǲ������tuple���Ǹ�task��
//				 *  �������һ��spout���ֳɺܶ��task��ִ�У� 
//				 *  ��Ϣִ�еĳɹ�ʧ�����ʼ�ջ�֪ͨ�ʼ����tuple���Ǹ�task
//				 */
//				this.collector.emit(new Values(str),str);
//			}
//		}catch(Exception e){
//			throw new RuntimeException("Error reading tuple",e);
//		}finally{
//			completed = true;
//		}
//	}
	
	  @Override
	  public void nextTuple() {
	    try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    String[] sentences = new String[]{ "i am ", "zhe800",
	        "tuan800 taobao", "changyou jd", "520 405 531 412 409" };
	    String sentence = sentences[new Random().nextInt(sentences.length)];
	    this.collector.emit(new Values(sentence));
	  }

	/**
	 * We will create the file and get the collector object
	 */
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	/**
	 * Declare the output field "word"
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
}
