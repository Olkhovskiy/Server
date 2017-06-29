import org.w3c.dom.Document;

import javax.print.Doc;
import javax.xml.transform.TransformerException;

/**
 * Created by Васили on 23.06.2017.
 */
public class Server {
    public static void main(String[] args) {
        KafkaConnect kafkaConnect = new KafkaConnect();
        Course course = new Course();
        Document doc = null;
        Document newDoc = null;
        String path;
        while (true) {
            try {
                doc = kafkaConnect.read();

                if (doc != null) {
                    System.out.println("read OK");
                    path = course.change(doc);
                    System.out.println("change OK");
                    if (path == "success.xml") {
                        kafkaConnect.write(path);
                        System.out.println("write OK");
                    } else System.out.println("Error change");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
