package com.s.fivebrains.atompaymentgateway;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.atom.mobilepaymentsdk.PayActivity;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    Button payMerchantNB;
    Button payMerchantDC;
    Spinner Bank;
    Spinner cardType;
    Spinner PaymentType;
    Spinner PaymentOption;
    String mprod;
    String amt = null;
    String strPayment="";
    EditText et_nb_amt, et_card_amt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String createXmlForProducts() throws Exception{
        // TODO Auto-generated method stub

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Here instead of parsing an existing document we want to
        // create a new one.
        Document testDoc = builder.newDocument();

        // This creates a new tag named 'testElem' inside
        // the document and sets its data to 'TestContent'
        ArrayList<String> lst = new ArrayList<String>();
        lst.add("1,One,250,1,2");
        lst.add("2,Two,250,1,2,3,4,5");

        //		lst.add("3,Three,500");

        //		String[] input = {"1,One,250,1,2,3,4,5", "2,Two,250,1,2,3,4,5", "3,Three,250,1,2,3,4,5"};
        //		String[] line = new String[8];
        int doubleAmt = 0;
        Element products = testDoc.createElement("products");
        testDoc.appendChild(products);

        for(String s: lst)
        {
            String line[] = s.split(",");

            //		for(int i = 0; i < lst.size(); i++){
            //
            //			line = lst.get(i).split(",");
            Element product = testDoc.createElement("product");

            products.appendChild(product);

            Element id = testDoc.createElement("id");
            id.appendChild(testDoc.createTextNode(line[0]));
            product.appendChild(id);

            Element name = testDoc.createElement("name");
            name.appendChild(testDoc.createTextNode(line[1]));
            product.appendChild(name);

            Element amount = testDoc.createElement("amount");
            amount.appendChild(testDoc.createTextNode(line[2]));
            product.appendChild(amount);

            doubleAmt = doubleAmt + Integer.parseInt(line[2]);
            //			amt = amt + line[2];
            amt = Integer.toString(doubleAmt);

            if(line.length > 3){
                Element param1 = testDoc.createElement("param1");
                param1.appendChild(testDoc.createTextNode(line[3]));
                product.appendChild(param1);
            }

            if(line.length > 4){
                Element param2 = testDoc.createElement("param2");
                param2.appendChild(testDoc.createTextNode(line[4]));
                product.appendChild(param2);
            }

            if(line.length > 5){
                Element param3 = testDoc.createElement("param3");
                param3.appendChild(testDoc.createTextNode(line[5]));
                product.appendChild(param3);
            }

            if(line.length > 6){
                Element param4 = testDoc.createElement("param4");
                param4.appendChild(testDoc.createTextNode(line[6]));
                product.appendChild(param4);
            }

            if(line.length > 7){
                Element param5 = testDoc.createElement("param5");
                param5.appendChild(testDoc.createTextNode(line[7]));
                product.appendChild(param5);
            }
        }

        System.out.println("Total Amount :::" +amt);


        try{
            DOMSource source = new DOMSource(testDoc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(source, result);
            writer.flush();
            //		        	System.out.println( writer.toString());
            String s = writer.toString().split("\\?")[2].substring(1,writer.toString().split("\\?")[2].length());
            //		        	wslog.writelog(Priority.INFO,"passDetailsXmlRequest", s);

            System.out.println("Product XML : " +s);
            return s;
        }
        catch(TransformerException ex) {
            ex.printStackTrace();
            return null;
        }


    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("In On Resume");
        setContentView(R.layout.mainpage);

        try {
            mprod = createXmlForProducts();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        et_nb_amt = (EditText) findViewById(R.id.et_nb_amt);
        et_nb_amt.setText(amt);
        et_card_amt = (EditText) findViewById(R.id.et_card_amt);

        cardType = (Spinner) findViewById(R.id.sp_cardType);
        PaymentType = (Spinner)findViewById(R.id.sp_paymentType);
        Bank = (Spinner)findViewById(R.id.sp_bank);
        PaymentOption = (Spinner)findViewById(R.id.sp_payment_option);
        payMerchantNB = (Button) findViewById(R.id.btn_payMerchantNB);





        PaymentOption.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                switch (position) {

                    case 1:

                        Bank.setVisibility(View.GONE);
                        break;

                    case 2:

                        Bank.setVisibility(View.VISIBLE);
                        break;
                    case 3:

                        Bank.setVisibility(View.GONE);
                        break;

                    case 4:

                        Bank.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        payMerchantNB.setOnClickListener(new OnClickListener() {




            public void onClick(View v)
            {
                amt = et_nb_amt.getText().toString();

                int payOption = PaymentOption.getSelectedItemPosition();


                switch(payOption)
                {
                    case 1:
                        strPayment = "All";
                        break;
                    case 2:
                        strPayment = "NB";
                        break;
                    case 3:
                        strPayment = "IMPS";
                        break;
                    //				case 3:
                    //					strPayment = "WALLET";
                    //					break;
                }

                if(amt.equalsIgnoreCase(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
                }
                //				else if(Bank.getSelectedItemPosition()==0)
                //				{
                //					Toast.makeText(MPSActivity.this, "Please select valid bank", Toast.LENGTH_LONG).show();
                //				}
                else if(PaymentOption.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(MainActivity.this, "Please select Payment option", Toast.LENGTH_LONG).show();
                }
                else
                {

                    Double doubleAmt = Double.valueOf(amt);
                    amt = doubleAmt.toString();
                    String bankId = "2001";


//					Intent newPayIntent = new Intent(MPSActivity.this,	PayActivity.class);
//
//			        newPayIntent.putExtra("merchantId", "1191");
//			        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
//			        newPayIntent.putExtra("loginid", "1191");
//			        newPayIntent.putExtra("password", "NCA@1234");
//			        newPayIntent.putExtra("prodid", "NCA");
//			        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
//			        newPayIntent.putExtra("clientcode", "001");
//			        newPayIntent.putExtra("custacc", "100000036600");
//			        newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
//			        newPayIntent.putExtra("txnid", "662016053501");
//			        newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
////			        newPayIntent.putExtra("bankid", "1013"); //Should be valid bank id
//			        newPayIntent.putExtra("discriminator", strPayment);
//
//
//			        //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
//			        newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
////			        newPayIntent.putExtra("ru","https://sslpayment.atomtech.in/mobilesdk/param"); //ru FOR Production
//
//			        //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
////			        newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)
//
//			        //Optinal Parameters
//			        newPayIntent.putExtra("customerName", "JKL PQR"); //Only for Name
//			        newPayIntent.putExtra("customerEmailID", "jkl.pqr@atomtech.in");//Only for Email ID
//			        newPayIntent.putExtra("customerMobileNo", "9876543210");//Only for Mobile Number
//			        newPayIntent.putExtra("billingAddress", "Mumbai");//Only for Address
//			        newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 1");// Can pass any data
////			        newPayIntent.putExtra("mprod", mprod); // Pass data in XML format, only for Multi product
//
//			        startActivityForResult(newPayIntent, 1);



                    Intent newPayIntent = new Intent(MainActivity.this,	PayActivity.class);

                    newPayIntent.putExtra("merchantId", "160");
                    newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
                    newPayIntent.putExtra("loginid", "160");
// 				    newPayIntent.putExtra("loginid", "2"); //for Multi product
                    newPayIntent.putExtra("password", "Test@123");
                    newPayIntent.putExtra("prodid", "NSE");
//			        newPayIntent.putExtra("prodid", "Multi");
                    newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
                    newPayIntent.putExtra("clientcode", encodeBase64("001"));
                    newPayIntent.putExtra("custacc", "100000036600");
                    newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
                    newPayIntent.putExtra("txnid", "013");
                    newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
                    newPayIntent.putExtra("bankid", bankId); //Should be valid bank id
                    newPayIntent.putExtra("discriminator", strPayment); //NB/ IMPS/ All

//			        //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
//			        //newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
                    //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
                    newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)

                    //Optinal Parameters
                    newPayIntent.putExtra("customerName", "JKL PQR"); //Only for Name
                    newPayIntent.putExtra("customerEmailID", "jkl.pqr@atomtech.in");//Only for Email ID
                    newPayIntent.putExtra("customerMobileNo", "9876543210");//Only for Mobile Number
                    newPayIntent.putExtra("billingAddress", "Mumbai");//Only for Address
                    newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 1");// Can pass any data
                    newPayIntent.putExtra("mprod", mprod); // Pass data in XML format, only for Multi product

                    startActivityForResult(newPayIntent, 1);
                }
            }
        });

        payMerchantDC = (Button) findViewById(R.id.btn_payMerchantDC);
        payMerchantDC.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                String amt = et_card_amt.getText().toString();
                String strPaymentMode = null, strCardType = "";
                int PaymentTypePos = PaymentType.getSelectedItemPosition();
                int cardTypePos = cardType.getSelectedItemPosition();

                if(amt.equalsIgnoreCase(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
                }
                else if(PaymentTypePos==0)
                {
                    Toast.makeText(MainActivity.this, "Please select valid Payment Mode", Toast.LENGTH_LONG).show();
                }
//				else if(cardTypePos==0)
//				{
//					Toast.makeText(MPSActivity.this, "Please select valid Card Type", Toast.LENGTH_LONG).show();
//				}
                else
                {
                    Double doubleAmt = Double.valueOf(amt);
                    amt = doubleAmt.toString();

                    switch(PaymentTypePos)
                    {
                        case 1:
                            strPaymentMode = "CC";
                            break;
                        case 2:
                            strPaymentMode = "DC";
                            break;
                    }


                    switch(cardTypePos)
                    {
                        case 1:
                            strCardType = "VISA";
                            break;
                        case 2:
                            strCardType = "MAESTRO";
                            break;
                        case 3:
                            strCardType = "MASTER";
                            break;
                    }


//					Intent newPayIntent = new Intent(MPSActivity.this,	PayActivity.class);
//					newPayIntent.putExtra("merchantId", "1191");
//					newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
//					newPayIntent.putExtra("loginid", "1191");
//					newPayIntent.putExtra("password", "NCA@1234");
//					newPayIntent.putExtra("prodid", "NCA");
//					newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
//					newPayIntent.putExtra("clientcode", "001");
//					newPayIntent.putExtra("custacc", "100000036600");
//					newPayIntent.putExtra("channelid", "INT");
//					newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
//					newPayIntent.putExtra("txnid", "662016053501");
//					newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
//					newPayIntent.putExtra("cardtype", strPaymentMode);// CC or DC ONLY (value should be same as commented)
//					newPayIntent.putExtra("cardAssociate", strCardType);// VISA or MASTER or MAESTRO ONLY (value should be same as commented)
//					newPayIntent.putExtra("surcharge", "NO");
//
//					//use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
//					newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
//		//			        newPayIntent.putExtra("ru","https://sslpayment.atomtech.in/mobilesdk/param"); //ru FOR Production
//
//										        //Optinal Parameters
//					newPayIntent.putExtra("customerName", "LMN PQR");//Only for Name
//					newPayIntent.putExtra("customerEmailID", "pqr.lmn@atomtech.in");//Only for Email ID
//					newPayIntent.putExtra("customerMobileNo", "9978868666");//Only for Mobile Number
//					newPayIntent.putExtra("billingAddress", "Pune");//Only for Address
//					newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");// Can pass any data
//		//			        newPayIntent.putExtra("mprod", mprod);  // Pass data in XML format, only for Multi product
//
//					startActivityForResult(newPayIntent, 1);
//
                    System.out.println("strCardType ::"+strCardType);

                    Intent newPayIntent = new Intent(MainActivity.this,	PayActivity.class);
                    newPayIntent.putExtra("merchantId", "160");
                    newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
                    newPayIntent.putExtra("loginid", "160");
                    newPayIntent.putExtra("password", "Test@123");
                    newPayIntent.putExtra("prodid", "NSE");
//					newPayIntent.putExtra("prodid", "Multi");
                    newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
                    newPayIntent.putExtra("clientcode", encodeBase64("007"));
                    newPayIntent.putExtra("custacc", "100000036600");
                    newPayIntent.putExtra("channelid", "INT");
                    newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
                    newPayIntent.putExtra("txnid", "2365F315");
                    newPayIntent.putExtra("date", "30/12/2015 18:31:00");//Should be in same format
                    newPayIntent.putExtra("cardtype", strPaymentMode);// CC or DC ONLY (value should be same as commented)
                    newPayIntent.putExtra("cardAssociate", strCardType);// VISA or MASTER or MAESTRO ONLY (value should be same as commented)
                    newPayIntent.putExtra("surcharge", "NO");

                    //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
                    //newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production

                    //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
                    newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)

                    //Optinal Parameters
                    newPayIntent.putExtra("customerName", "LMN PQR");//Only for Name
                    newPayIntent.putExtra("customerEmailID", "pqr.lmn@atomtech.in");//Only for Email ID
                    newPayIntent.putExtra("customerMobileNo", "9978868666");//Only for Mobile Number
                    newPayIntent.putExtra("billingAddress", "Pune");//Only for Address
                    newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");// Can pass any data
                    newPayIntent.putExtra("mprod", mprod);  // Pass data in XML format, only for Multi product

                    startActivityForResult(newPayIntent, 1);


                }
            }
        });
    }

    public String encodeBase64(String encode)
    {
        System.out.println("[encodeBase64] Base64 encode : "+encode);
        String decode=null;

        try {


//			decode= new sun.misc.BASE64Encoder().encode(encode.getBytes());
            decode=  Base64.encode(encode.getBytes());
        } catch (Exception e) {
            System.out.println("Unable to decode : "+ e);
        }
        return  decode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed here it is 2
        System.out.println("RESULTCODE--->" + resultCode);
        System.out.println("REQUESTCODE--->" + requestCode);
        System.out.println("RESULT_OK--->" + RESULT_OK);


        if (requestCode == 1) {
            System.out.println("---------INSIDE-------");

            if (data != null) {
                String message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");

                //				Map<String, String> map = (Map<String, String>) data.getSerializableExtra("Data");
                //
                //				String f_code = map.get("f_code");
                //				System.out.println("f_code ::"+f_code);
                //
                //				Set<String> keySet = map.keySet();
                //
                //				for(String s : keySet){
                //
                //					String value = map.get(s);
                //					System.out.println("Key :"+s +"\t value :"+value);
                //				}

                if(resKey!=null && resValue!=null)
                {
                    for(int i=0; i<resKey.length; i++)
                        System.out.println("  "+i+" resKey : "+resKey[i]+" resValue : "+resValue[i]);
                }
                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                //  System.out.println("RECEIVED BACK--->" + message);

                System.out.println("==========message======="+message);
            }

        }

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.m, menu);
        return true;
    }*/

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
