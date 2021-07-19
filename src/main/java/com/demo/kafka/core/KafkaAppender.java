package com.demo.kafka.core;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.demo.kafka.config.KafkaConfigHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 *
 * @author Dean
 * @date 2018-05-07
 */
@Slf4j
public class KafkaAppender extends ConsoleAppender<ILoggingEvent> {

	private String bootstrapServers;
	private String topic;
	private String batchSize;
	private String lingerMs;
	private String compressionType;
	private String retries;
	private String maxRequestSize;
	private String isSend;

	private Producer<String, String> producer;

	@Override
	public String toString() {
		return "KafkaAppender{" +
				"bootstrapServers='" + bootstrapServers + '\'' +
				", topic='" + topic + '\'' +
				", batchSize='" + batchSize + '\'' +
				", lingerMs='" + lingerMs + '\'' +
				", compressionType='" + compressionType + '\'' +
				", retries='" + retries + '\'' +
				", maxRequestSize='" + maxRequestSize + '\'' +
				", isSend='" + isSend + '\'' +
				", producer=" + producer +
				'}';
	}

	@Override
	public void start() {
		super.start();
		if ("true".equals(this.isSend)) {
			if (producer == null) {
				producer = KafkaConfigHelper.createProducer(this.bootstrapServers);
			}
		}
	}

	@Override
	public void stop() {
		super.stop();
		if ("true".equals(this.isSend)) {
			this.producer.close();
		}
		log.info("Stopping kafkaAppender...");
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		byte[] byteArray;
		String logMessage;
		// 对日志格式进行解码
		byteArray = this.encoder.encode(eventObject);
		logMessage = new String(byteArray);
		ProducerRecord<String, String> record = new ProducerRecord<>(this.topic, logMessage);
		if (eventObject.getMarker() == null && "true".equals(this.isSend)) {
			producer.send(record, new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if (exception != null) {
						log.error("发送kafka的日志异常：{}",logMessage);
					}
				}
			});
		}
	}

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(String batchSize) {
		this.batchSize = batchSize;
	}

	public String getLingerMs() {
		return lingerMs;
	}

	public void setLingerMs(String lingerMs) {
		this.lingerMs = lingerMs;
	}

	public String getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(String compressionType) {
		this.compressionType = compressionType;
	}

	public String getRetries() {
		return retries;
	}

	public void setRetries(String retries) {
		this.retries = retries;
	}

	public String getMaxRequestSize() {
		return maxRequestSize;
	}

	public void setMaxRequestSize(String maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}

	public Producer<String, String> getProducer() {
		return producer;
	}

	public void setProducer(Producer<String, String> producer) {
		this.producer = producer;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

}
