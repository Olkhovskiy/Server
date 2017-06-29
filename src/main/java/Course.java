import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Created by Васили on 23.06.2017.
 */
public class Course {
    public static double currentCourse;
    public static double oldCourse;
    public Course(){
        int a = 30;
        int b = 150;

        this.currentCourse = a + (Math.random() * b);
        this.oldCourse = this.currentCourse;
        System.out.println("Current course: " + this.currentCourse);
    }

    public String change (Document doc){
        Node type,value;
        boolean flag=false;
        if(doc !=null){
            type=doc.getChildNodes().item(0);
            if(type.getAttributes().getNamedItem("type").getNodeValue()=="addition") {
                this.oldCourse=this.currentCourse;
                this.currentCourse += Double.parseDouble(type.getAttributes().getNamedItem("value").getNodeValue());
                flag=true;
            }
            else if(type.getAttributes().getNamedItem("type").getNodeValue()=="subtraction") {
                this.oldCourse=this.currentCourse;
                this.currentCourse -= Double.parseDouble(type.getAttributes().getNamedItem("value").getNodeValue());
                flag=true;
            }
            else this.currentCourse=this.oldCourse;
            flag=true;

        }
        if (flag) return ("success.xml");
        else return "Error";


}}
