# Atom-Payment-Gateway


# Atom Payment Gateway Integration process

Add jar file in your project libs folder


1.MobilePaymentSDKOutput.jar

2. secure-data.jar

3. xercesImpl-2.6.2-jaxb-1.0.6.jar

and include asdk(Atom Sdk) dependency your project gradle file
After adding jar file.

After include jar file and dependency your project build gradle file showing.

    implementation project(path: ':asdk')
    implementation files('libs\\MobilePaymentSDKOutput.jar')
    implementation files('libs\\secure-data.jar')
    implementation files('libs\\xercesImpl-2.6.2-jaxb-1.0.6.jar')
    
    
   
  # If you are getting this issue android 9 above version 
  java.lang.classnotfoundexception: didn't find class "org.apache.http.util.encodingutils"
  # add library in your manifest file
 
 ```manifest

        <uses-library android:name="org.apache.http.legacy" android:required="false"/>


```

    
    
   

