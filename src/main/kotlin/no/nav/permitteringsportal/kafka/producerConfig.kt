package no.nav.permitteringsportal.kafka

import no.nav.permitteringsportal.utils.Environment
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs
import java.util.*
import org.apache.kafka.common.serialization.StringSerializer

fun producerConfig() = Properties().apply {
    put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, Environment.get("KAFKA_KEYSTORE_PATH"));
    put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,Environment.get("KAFKA_CREDSTORE_PASSWORD"));
    put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, Environment.get("KAFKA_CREDSTORE_PASSWORD"));
    put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "KAFKA_KEY_PASSWORD_CONFIG");
    put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
    put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "PKCS12");
    put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "KAFKA_TRUSTSTORE_PATH");

    put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,"SSL");

    put(ProducerConfig.CLIENT_ID_CONFIG, "permitteringsportal-api");

    //
    put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java);
    put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer::class.java);
    put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Environment.get("KAFKA_BROKERS"));
}
