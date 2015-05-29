package com.lsh.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * �����־���
 * @author Luoshuhong
 * @Company  
 * 2015��5��29��
 *
 */
public class WordNormalizer extends BaseBasicBolt {
	OutputCollector _collector;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		super.prepare(stormConf, context);
	}
	
	public void cleanup() {}
	/**
	 * The bolt will receive the line from the
	 * words file and process it to Normalize this line
	 * The normalize will be put the words in lower case
	 * and split the line to get all words in this 
	 */
	public void execute(Tuple input, BasicOutputCollector collector) {
        String sentence = input.getString(0);
        String[] words = sentence.split(" ");
        for(String word : words){
            word = word.trim();
            if(!word.isEmpty()){
                word = word.toLowerCase();
                collector.emit(new Values(word));
                
                //���ʧ�� ����ֱ�ӵ���fail����  �������԰ڿ������´���
                //collector.getOutputter().fail(input);
            }
        }
	}
	
	/**
	 * The bolt will only emit the field "word" 
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));  //����������ֶ�
	}
}
