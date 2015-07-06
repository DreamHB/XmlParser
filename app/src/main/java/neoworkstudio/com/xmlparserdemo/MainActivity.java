package neoworkstudio.com.xmlparserdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends AppCompatActivity {

    private InputStream is = null;
    private List<Staff> staffs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            is = getAssets().open("address.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        staffs = new ArrayList<>();
        //readXMLPull();
        readXMLSAX();
        //readXMLDom();
        printResult();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readXMLPull(){
        Staff staff = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("MainActivity", " name = " + parser.getName());
                        staffs = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        Log.d("MainActivity", " name = " + parser.getName());
                        switch (parser.getName()){
                            case "staff":
                                staff = new Staff();
                                break;
                            case "name":
                                staff.setName(parser.nextText());
                                break;
                            case "gender":
                                staff.setGender(parser.nextText());
                                break;
                            case "age":
                                staff.setAge(Integer.valueOf(parser.nextText()));
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("staff")){
                            staffs.add(staff);
                            staff = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readXMLDom(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(is);
            Element element = document.getDocumentElement();
            NodeList list = element.getElementsByTagName("staff");
            for(int i = 0, len = list.getLength(); i < len; i++){
                Staff staff = new Staff();
                Node node = list.item(i);
                NodeList childList = node.getChildNodes();
                for(int j = 0, lenJ = childList.getLength(); j < lenJ; j++) {
                    Node childNode = childList.item(j);
                    Log.d("MainActivity", " childNode name = " + childNode.getNodeName() + " childNode value = " + childNode.getTextContent());
                    switch (childNode.getNodeName()) {
                        case "name":
                            staff.setName(childNode.getTextContent());
                            break;
                        case "gender":
                            staff.setGender(childNode.getTextContent());
                            break;
                        case "age":
                            staff.setAge(Integer.valueOf(childNode.getTextContent()));
                            break;
                    }
                }
                staffs.add(staff);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    private void readXMLSAX(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            StaffContentHandler handler = new StaffContentHandler();
            parser.parse(is, handler);
            //XMLReader reader = parser.getXMLReader();
            //reader.setContentHandler();
            staffs = handler.getStaffs();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void printResult(){
        for(Staff s: staffs){
            Log.d("MainActivity", " s = " + s);
        }
    }

    class StaffContentHandler extends DefaultHandler {
        private List<Staff> staffs;
        private Staff staff;
        private StringBuilder stringBuilder;

        public List<Staff> getStaffs(){
            return staffs;
        }
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            staffs = new ArrayList<>();
            stringBuilder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("staff")){
                staff = new Staff();
            }
            stringBuilder.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            stringBuilder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            switch (localName){
                case "name":
                    staff.setName(stringBuilder.toString());
                    break;
                case "gender":
                    staff.setGender(stringBuilder.toString());
                    break;
                case "age":
                    staff.setAge(Integer.valueOf(stringBuilder.toString()));
                    break;
                case "staff":
                    staffs.add(staff);
                    break;
            }
        }
    }
}
