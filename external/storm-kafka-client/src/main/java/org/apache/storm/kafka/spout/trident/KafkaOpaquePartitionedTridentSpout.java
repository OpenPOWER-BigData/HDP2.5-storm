/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.apache.storm.kafka.spout.trident;

import org.apache.kafka.common.TopicPartition;
import org.apache.storm.kafka.spout.KafkaSpoutStreams;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.trident.spout.IOpaquePartitionedTridentSpout;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class KafkaOpaquePartitionedTridentSpout<K,V> implements IOpaquePartitionedTridentSpout<List<TopicPartition>, TopicPartitionTridentSpout, MetadataTridentSpout<K,V>> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaOpaquePartitionedTridentSpout.class);

    private KafkaManagerTridentSpout<K, V> kafkaManager;

    public KafkaOpaquePartitionedTridentSpout(KafkaManagerTridentSpout<K, V> kafkaManager) {
        this.kafkaManager = kafkaManager;
        LOG.debug("Created {}", this);
    }

    @Override
    public Emitter<List<TopicPartition>, TopicPartitionTridentSpout, MetadataTridentSpout<K,V>> getEmitter(Map conf, TopologyContext context) {
        return new EmitterTridentSpout<>(kafkaManager);
    }

    @Override
    public Coordinator<List<TopicPartition>> getCoordinator(Map conf, TopologyContext context) {
        return new CoordinatorTridentSpout<>(kafkaManager);
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public Fields getOutputFields() {
        final KafkaSpoutStreams kafkaSpoutStreams = kafkaManager.getKafkaSpoutStreams();
        final Fields outputFields = kafkaSpoutStreams.getOutputFields();
        LOG.debug("OutputFields = {}", outputFields);
        return outputFields;
    }

    @Override
    public String toString() {
        return "KafkaOpaquePartitionedTridentSpout{" +
                "kafkaManager=" + kafkaManager +
                '}';
    }
}
