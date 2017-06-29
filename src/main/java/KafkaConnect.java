import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by Васили on 23.06.2017.
 */
public class KafkaConnect {
 static String topicName = "test";
        public Document read () throws TransformerException {
            //if(args.length == 0){
//            System.out.println("Enter topic name");
//            return;
//        }
                //Kafka consumer configuration settings
                //String topicName = args[0].toString();
                this.topicName = "toServer";
                Properties props = new Properties();

                props.put("bootstrap.servers", "localhost:9092");
                props.put("group.id", "test");
                props.put("enable.auto.commit", "true");
                props.put("auto.commit.interval.ms", "1000");
                props.put("session.timeout.ms", "30000");
                props.put("key.deserializer",
                        "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("value.deserializer",
                        "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("auto.offset.reset","earliest");

                KafkaConsumer<String, String> consumer = new KafkaConsumer
                        <String, String>(props);

                //Kafka Consumer subscribes list of topics here.
                consumer.subscribe(Arrays.asList(topicName));

                //print the topic name
                // System.out.println("Subscribed to topic " + topicName);
                int i = 0;
                Document doc = null;
                    ConsumerRecords<String, String> records =consumer.poll(500);

                    for (ConsumerRecord<String, String> record : records) {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder;

                        try
                        {
                            builder = factory.newDocumentBuilder();
                            doc = builder.parse( new InputSource( new StringReader( record.value() )) );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                      //  StreamResult result = new StreamResult(new File(("read.xml")));
                      //  TransformerFactory transformerFactory = TransformerFactory.newInstance();
                       // Transformer transformer = transformerFactory.newTransformer();
                       // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                       // transformer.transform(new DOMSource(doc), result);
                    }
        return doc;
        }

    public void write(String path ) throws Exception{
        this.topicName = "fromServer";

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
       // props.put("bootstrap.servers", "192.168.181.133:9092");

        props.put("bootstrap.servers", "localhost:9092");
        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder;
        builder = f.newDocumentBuilder();
        Document doc = builder.parse(new File(path));
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        StringWriter sw = new StringWriter();
        trans.transform(new DOMSource(doc), new StreamResult(sw));

        for(int i = 0; i < 10; i++)
            producer.send(new ProducerRecord(this.topicName,sw.toString()));

        System.out.println("Message sent successfully");
        producer.close();
    }
}
