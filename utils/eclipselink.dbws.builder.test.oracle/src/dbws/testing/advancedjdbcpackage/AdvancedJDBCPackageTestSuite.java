/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Mike Norman - Nov.9, 2011 - 2.4: test use of Advanced JDBC types from
 *                                      within a PL/SQL Package
 ******************************************************************************/
package dbws.testing.advancedjdbcpackage;

//javase imports
import java.io.StringReader;
import org.w3c.dom.Document;

//java eXtension imports
import javax.wsdl.WSDLException;

//JUnit4 imports
import org.junit.AfterClass;
import org.junit.BeforeClass;
//import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//EclipseLink imports
import org.eclipse.persistence.internal.xr.Invocation;
import org.eclipse.persistence.internal.xr.Operation;
import org.eclipse.persistence.oxm.XMLMarshaller;
import org.eclipse.persistence.oxm.XMLUnmarshaller;

//testing imports
import dbws.testing.DBWSTestSuite;

public class AdvancedJDBCPackageTestSuite extends DBWSTestSuite {

    static final String CREATE_REGION_TYPE =
        "CREATE OR REPLACE TYPE REGION AS OBJECT (" +
            "\nREG_ID NUMBER(5)," +
            "\nREG_NAME VARCHAR2(50)" +
        "\n)";
    static final String CREATE_EMP_ADDRESS_TYPE =
        "CREATE OR REPLACE TYPE EMP_ADDRESS AS OBJECT (" +
            "\nSTREET VARCHAR2(100)," +
            "\nSUBURB VARCHAR2(100)," +
            "\nADDR_REGION REGION," +
            "\nPOSTCODE INTEGER" +
        "\n)";
    static final String CREATE_EMP_OBJECT_TYPE =
        "CREATE OR REPLACE TYPE EMP_OBJECT AS OBJECT (" +
            "\nEMPLOYEE_ID NUMBER(8)," +
            "\nADDRESS EMP_ADDRESS," +
            "\nEMPLOYEE_NAME VARCHAR2(80)," +
            "\nDATE_OF_HIRE  DATE" +
        "\n)";
    static final String CREATE_EMP_INFO_TYPE =
        "CREATE OR REPLACE TYPE EMP_INFO AS OBJECT (" +
            "\nID NUMBER(5)," +
            "\nNAME VARCHAR2(50)" +
        "\n)";
    static final String CREATE_EMP_INFO_ARRAY_TYPE =
        "CREATE OR REPLACE TYPE EMP_INFO_ARRAY AS VARRAY(3) OF EMP_INFO";
    static final String CREATE_ADVANCED_OBJECT_DEMO_PACKAGE =
        "CREATE OR REPLACE PACKAGE ADVANCED_OBJECT_DEMO AS" +
            "\nFUNCTION ECHOREGION(AREGION IN REGION) RETURN REGION;" +
            "\nFUNCTION ECHOEMPADDRESS(ANEMPADDRESS IN EMP_ADDRESS) RETURN EMP_ADDRESS;" +
            "\nFUNCTION ECHOEMPOBJECT(ANEMPOBJECT IN EMP_OBJECT) RETURN EMP_OBJECT;" +
            "\nFUNCTION BUILDEMPARRAY(NUM IN INTEGER) RETURN EMP_INFO_ARRAY;" +
        "\nEND ADVANCED_OBJECT_DEMO;";
    static final String CREATE_ADVANCED_OBJECT_DEMO_BODY =
        "CREATE OR REPLACE PACKAGE BODY ADVANCED_OBJECT_DEMO AS" +
            "\nFUNCTION ECHOREGION(AREGION IN REGION) RETURN REGION AS" +
            "\nBEGIN" +
                "\nRETURN AREGION;" +
            "\nEND ECHOREGION;" +
            "\nFUNCTION ECHOEMPADDRESS(ANEMPADDRESS IN EMP_ADDRESS) RETURN EMP_ADDRESS AS" +
            "\nBEGIN" +
                "\nRETURN ANEMPADDRESS;" +
            "\nEND ECHOEMPADDRESS;" +
            "\nFUNCTION ECHOEMPOBJECT(ANEMPOBJECT IN EMP_OBJECT) RETURN EMP_OBJECT AS" +
            "\nBEGIN" +
                "\nRETURN ANEMPOBJECT;" +
            "\nEND ECHOEMPOBJECT;" +
            "\nFUNCTION BUILDEMPARRAY(NUM IN INTEGER) RETURN EMP_INFO_ARRAY AS" +
            "\nL_DATA EMP_INFO_ARRAY := EMP_INFO_ARRAY();" +
            "\nBEGIN" +
                "\nFOR I IN 1 .. NUM LOOP" +
                    "\nL_DATA.EXTEND;" +
                    "\nL_DATA(I) := EMP_INFO(I, 'entry ' || i);" +
                "\nEND LOOP;" +
                "\nRETURN L_DATA;" +
            "\nEND BUILDEMPARRAY;" +
        "\nEND ADVANCED_OBJECT_DEMO;";
    static final String DROP_REGION_TYPE =
        "DROP TYPE REGION";
    static final String DROP_EMP_ADDRESS_TYPE =
        "DROP TYPE EMP_ADDRESS";
    static final String DROP_EMP_OBJECT_TYPE =
        "DROP TYPE EMP_OBJECT";
    static final String DROP_EMP_INFO_ARRAY_TYPE =
        "DROP TYPE EMP_INFO_ARRAY";
    static final String DROP_EMP_INFO_TYPE =
        "DROP TYPE EMP_INFO";
    static final String DROP_ADVANCED_OBJECT_DEMO_PACKAGE =
        "DROP PACKAGE ADVANCED_OBJECT_DEMO";

    static boolean ddlCreate = false;
    static boolean ddlDrop = false;
    static boolean ddlDebug = false;

    @BeforeClass
    public static void setUp() throws WSDLException {
        if (conn == null) {
            try {
                conn = buildConnection();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        String ddlCreateProp = System.getProperty(DATABASE_DDL_CREATE_KEY, DEFAULT_DATABASE_DDL_CREATE);
        if ("true".equalsIgnoreCase(ddlCreateProp)) {
            ddlCreate = true;
        }
        String ddlDropProp = System.getProperty(DATABASE_DDL_DROP_KEY, DEFAULT_DATABASE_DDL_DROP);
        if ("true".equalsIgnoreCase(ddlDropProp)) {
            ddlDrop = true;
        }
        String ddlDebugProp = System.getProperty(DATABASE_DDL_DEBUG_KEY, DEFAULT_DATABASE_DDL_DEBUG);
        if ("true".equalsIgnoreCase(ddlDebugProp)) {
            ddlDebug = true;
        }
        if (ddlCreate) {
            runDdl(conn, CREATE_REGION_TYPE, ddlDebug);
            runDdl(conn, CREATE_EMP_ADDRESS_TYPE, ddlDebug);
            runDdl(conn, CREATE_EMP_OBJECT_TYPE, ddlDebug);
            runDdl(conn, CREATE_EMP_INFO_TYPE, ddlDebug);
            runDdl(conn, CREATE_EMP_INFO_ARRAY_TYPE, ddlDebug);
            runDdl(conn, CREATE_ADVANCED_OBJECT_DEMO_PACKAGE, ddlDebug);
            runDdl(conn, CREATE_ADVANCED_OBJECT_DEMO_BODY, ddlDebug);
        }
        DBWS_BUILDER_XML_USERNAME =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<dbws-builder xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
              "<properties>" +
                  "<property name=\"projectName\">advancedjdbcpackage</property>" +
                  "<property name=\"logLevel\">off</property>" +
                  "<property name=\"username\">";
          DBWS_BUILDER_XML_PASSWORD =
                  "</property><property name=\"password\">";
          DBWS_BUILDER_XML_URL =
                  "</property><property name=\"url\">";
          DBWS_BUILDER_XML_DRIVER =
                  "</property><property name=\"driver\">";
          DBWS_BUILDER_XML_PLATFORM =
                  "</property><property name=\"platformClassname\">";
          DBWS_BUILDER_XML_MAIN =
                  "</property>" +
              "</properties>" +
              "<procedure " +
                  "name=\"echoRegion\" " +
                  "catalogPattern=\"ADVANCED_OBJECT_DEMO\" " +
                  "procedurePattern=\"ECHOREGION\" " +
                  "isAdvancedJDBC=\"true\" " +
                  "returnType=\"regionType\" " +
              "/>" +
              "<procedure " +
                  "name=\"echoEmpAddress\" " +
                  "catalogPattern=\"ADVANCED_OBJECT_DEMO\" " +
                  "procedurePattern=\"ECHOEMPADDRESS\" " +
                  "isAdvancedJDBC=\"true\" " +
                  "returnType=\"emp_addressType\" " +
              "/>" +
              "<procedure " +
                  "name=\"echoEmpObject\" " +
                  "catalogPattern=\"ADVANCED_OBJECT_DEMO\" " +
                  "procedurePattern=\"ECHOEMPOBJECT\" " +
                  "isAdvancedJDBC=\"true\" " +
                  "returnType=\"emp_objectType\" " +
                  "/>" +
              "<procedure " +
                  "name=\"buildEmpArray\" " +
                  "catalogPattern=\"ADVANCED_OBJECT_DEMO\" " +
                  "procedurePattern=\"BUILDEMPARRAY\" " +
                  "isAdvancedJDBC=\"true\" " +
                  "returnType=\"emp_info_arrayType\" " +
              "/>" +
            "</dbws-builder>";
          builder = null;
          DBWSTestSuite.setUp(".");
    }

    @AfterClass
    public static void tearDown() {
        if (ddlDrop) {
            runDdl(conn, DROP_ADVANCED_OBJECT_DEMO_PACKAGE, ddlDebug);
            runDdl(conn, DROP_EMP_INFO_ARRAY_TYPE, ddlDebug);
            runDdl(conn, DROP_EMP_INFO_TYPE, ddlDebug);
            runDdl(conn, DROP_EMP_OBJECT_TYPE, ddlDebug);
            runDdl(conn, DROP_EMP_ADDRESS_TYPE, ddlDebug);
            runDdl(conn, DROP_REGION_TYPE, ddlDebug);
        }
    }

    @Test
    public void struct1LevelDeep() {
        XMLUnmarshaller unmarshaller = xrService.getXMLContext().createUnmarshaller();
        Object region = unmarshaller.unmarshal(new StringReader(REGION_XML));
        Invocation invocation = new Invocation("echoRegion");
        invocation.setParameter("AREGION", region);
        Operation op = xrService.getOperation(invocation.getName());
        Object result = op.invoke(xrService, invocation);
        assertNotNull("result is null", result);
        XMLMarshaller marshaller = xrService.getXMLContext().createMarshaller();
        Document doc = xmlPlatform.createDocument();
        marshaller.marshal(result, doc);
        Document controlDoc = xmlParser.parse(new StringReader(REGION_XML));
        assertTrue("Expected:\n" + documentToString(controlDoc) +
            "\nActual:\n" + documentToString(doc), comparer.isNodeEqual(controlDoc, doc));
    }
    static final String REGION_XML =
        REGULAR_XML_HEADER +
        "<regionType xmlns=\"urn:advancedjdbcpackage\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
        "<reg_id>5</reg_id>" +
        "<reg_name>this is a test</reg_name>" +
        "</regionType>";

    @Test
    public void struct2LevelDeep() {
        XMLUnmarshaller unmarshaller = xrService.getXMLContext().createUnmarshaller();
        Object empAddress = unmarshaller.unmarshal(new StringReader(EMP_ADDRESS_XML));
        Invocation invocation = new Invocation("echoEmpAddress");
        invocation.setParameter("ANEMPADDRESS", empAddress);
        Operation op = xrService.getOperation(invocation.getName());
        Object result = op.invoke(xrService, invocation);
        assertNotNull("result is null", result);
        XMLMarshaller marshaller = xrService.getXMLContext().createMarshaller();
        Document doc = xmlPlatform.createDocument();
        marshaller.marshal(result, doc);
        Document controlDoc = xmlParser.parse(new StringReader(EMP_ADDRESS_XML));
        assertTrue("Expected:\n" + documentToString(controlDoc) +
            "\nActual:\n" + documentToString(doc), comparer.isNodeEqual(controlDoc, doc));
    }
    static final String EMP_ADDRESS_XML =
        REGULAR_XML_HEADER +
        "<emp_addressType xmlns=\"urn:advancedjdbcpackage\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
           "<street>20 Pinetrail Cres.</street>" +
           "<suburb>Centrepointe</suburb>" +
           "<addr_region>" +
              "<reg_id>5</reg_id>" +
              "<reg_name>this is a test</reg_name>" +
           "</addr_region>" +
           "<postcode>12</postcode>" +
        "</emp_addressType>";

    @Test
    public void struct3LevelDeep() {
        XMLUnmarshaller unmarshaller = xrService.getXMLContext().createUnmarshaller();
        Object empObject = unmarshaller.unmarshal(new StringReader(EMP_OBJECT_XML));
        Invocation invocation = new Invocation("echoEmpObject");
        invocation.setParameter("ANEMPOBJECT", empObject);
        Operation op = xrService.getOperation(invocation.getName());
        Object result = op.invoke(xrService, invocation);
        assertNotNull("result is null", result);
        XMLMarshaller marshaller = xrService.getXMLContext().createMarshaller();
        Document doc = xmlPlatform.createDocument();
        marshaller.marshal(result, doc);
        Document controlDoc = xmlParser.parse(new StringReader(EMP_OBJECT_XML));
        assertTrue("Expected:\n" + documentToString(controlDoc) +
            "\nActual:\n" + documentToString(doc), comparer.isNodeEqual(controlDoc, doc));
    }
    static final String EMP_OBJECT_XML =
        REGULAR_XML_HEADER +
        "<emp_objectType xmlns=\"urn:advancedjdbcpackage\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
           "<employee_id>55</employee_id>" +
           "<address>" +
              "<street>20 Pinetrail Cres.</street>" +
              "<suburb>Centrepointe</suburb>" +
              "<addr_region>" +
                 "<reg_id>5</reg_id>" +
                 "<reg_name>this is a test</reg_name>" +
              "</addr_region>" +
              "<postcode>12</postcode>" +
           "</address>" +
           "<employee_name>Mike Norman</employee_name>" +
           "<date_of_hire>2011-11-11</date_of_hire>" +
        "</emp_objectType>";

    @Test
    public void buildEmpArray() {
        Invocation invocation = new Invocation("buildEmpArray");
        invocation.setParameter("NUM", Integer.valueOf(3));
        Operation op = xrService.getOperation(invocation.getName());
        Object result = op.invoke(xrService, invocation);
        assertNotNull("result is null", result);
        XMLMarshaller marshaller = xrService.getXMLContext().createMarshaller();
        Document doc = xmlPlatform.createDocument();
        marshaller.marshal(result, doc);
        Document controlDoc = xmlParser.parse(new StringReader(EMP_INFO_ARRAY_XML));
        assertTrue("Expected:\n" + documentToString(controlDoc) +
            "\nActual:\n" + documentToString(doc), comparer.isNodeEqual(controlDoc, doc));
    }
    static final String EMP_INFO_ARRAY_XML =
        REGULAR_XML_HEADER +
        "<emp_info_arrayType xmlns=\"urn:advancedjdbcpackage\">" +
            "<item>" +
                "<id>1</id>" +
                "<name>entry 1</name>" +
            "</item>" +
            "<item>" +
                "<id>2</id>" +
                "<name>entry 2</name>" +
            "</item>" +
            "<item>" +
                "<id>3</id>" +
                "<name>entry 3</name>" +
            "</item>" +
         "</emp_info_arrayType>";
}