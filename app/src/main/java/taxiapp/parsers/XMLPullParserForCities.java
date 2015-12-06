package taxiapp.parsers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import taxiapp.structures.GeoNameCity;

/**
 * Created by hassanjamil on 2015-12-06.
 */
public class XMLPullParserForCities {
    ArrayList<GeoNameCity> listCities;
    private GeoNameCity city;
    private String text;
    private String XMLTag_GeoName = "geoname";

    public XMLPullParserForCities() {
        listCities = new ArrayList<GeoNameCity>();
    }

    public ArrayList<GeoNameCity> getListCities() {
        return listCities;
    }

    public ArrayList<GeoNameCity> parse(InputStream is) {
        listCities.clear();

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase(XMLTag_GeoName)) {
                            // create a new instance of city
                            city = new GeoNameCity();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase(XMLTag_GeoName)) {
                            // add city object to list
                            listCities.add(city);
                        } else if (tagName.equalsIgnoreCase("toponymName")) {
                            city.setToponymName(text);
                        } else if (tagName.equalsIgnoreCase("name")) {
                            city.setName(text);
                        } else if (tagName.equalsIgnoreCase("lat")) {
                            city.setLat(Double.parseDouble(text));
                        } else if (tagName.equalsIgnoreCase("lng")) {
                            city.setLng(Double.parseDouble(text));
                        } else if (tagName.equalsIgnoreCase("geonameId")) {
                            city.setGeonameId(text);
                        } else if (tagName.equalsIgnoreCase("countryCode")) {
                            city.setCountryCode(text);
                        } else if (tagName.equalsIgnoreCase("countryName")) {
                            city.setCountryName(text);
                        } else if (tagName.equalsIgnoreCase("fcl")) {
                            city.setFcl(text);
                        } else if (tagName.equalsIgnoreCase("fcode")) {
                            city.setFcode(text);
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listCities;
    }
}
