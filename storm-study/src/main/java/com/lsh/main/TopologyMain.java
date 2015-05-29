package com.lsh.main;
import com.lsh.spout.WordReader;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.lsh.bolt.WordCounter;
import com.lsh.bolt.WordNormalizer;

/**
 * ��������
 * @author Luoshuhong
 * @Company  
 * 2015��5��29��
 *
 */
public class TopologyMain {
	public static void main(String[] args) throws InterruptedException {
         
        //Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader",new WordReader(), 2); //.setNumTasks(2); //����task����  ��������Ĭ��=executor
		//word-reader һ����   ����tuple�ᱻ����ķַ���bolt����� task
		//shuffle Grouping ��ʾ������� ��һ�����õ� bolt
		//fields grouping ��ʾ��ͬ�� tuple �ᷢ�͸�ͬһ��task
		builder.setBolt("word-normalizer", new WordNormalizer(), 6)
			.shuffleGrouping("word-reader"); 
		builder.setBolt("word-counter", new WordCounter(),4)  //1��ʾexecutor  ������
			.fieldsGrouping("word-normalizer", new Fields("word"));
		
        //Configuration
		Config conf = new Config();
//		conf.put("wordsFile", "D:/ѧϰ/storm/words.txt");
//		conf.setNumWorkers(3);
		conf.setMessageTimeoutSecs(20);  //һ��tuple����ʱʱ��  �ڸ�ʱ����û�д����� ����Ϊ����ʧ��
		//���� acker������  �˲�������Ϊ0 storm����spout����һ��tuple֮�����ϵ���spout��ack���� ���������ʧ�ܺ����ʧ���� ����������
		conf.setNumAckers(5);  
		//topology����������(spout, bolt)�趨һ���߳��������ޡ�һ����˵���ɻ�����������úܴ�(100����), ������ڱ��ز�����˵̫���ˣ� ������ÿ������������С
//		conf.setMaxTaskParallelism(10);
//		conf.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 10);  //Ч��ͬ��
		
//		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1); //�߳��� 
//		conf.setMaxSpoutPending(1);    //�߳���
		
//		conf.setDebug(false);
		conf.setDebug(true);
		
		//Զ���ύ
		if (args != null && args.length > 0) {
	      try {
	    	  conf.setNumWorkers(8); //����topology worker��
	    	  StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
	    } else {
	        //����ģʽ
	    	conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("word_count_test_lsh", conf, builder.createTopology());
			Thread.sleep(1000 * 10);
			cluster.killTopology("word_count_test_lsh");
			cluster.shutdown();
	    }
	}
}
