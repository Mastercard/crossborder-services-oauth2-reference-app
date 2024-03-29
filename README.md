## PSD2 Reference Implementation for Mastercard Cross-Border Services APIs
  
This is a reference application for PSD2 that demonstrates how Cross-border APIs can be used for the supported transaction types. Please see here for details on the APIs: [Mastercard Developers](https://developer.mastercard.com/cross-border-services/documentation/). 
This application illustrates connecting to the cross-border APIs with / without encryption.
Both the approaches require consumer key and .p12 file as received from [Mastercard Developers](https://developer.mastercard.com/dashboard).

### Frameworks / Libraries used

- [Spring Framework](https://projects.spring.io/spring-framework/) 5.2.X
  
### Requirements

- Java 8
- Apache maven 3.5.x  
- Set up the `JAVA_HOME` environment variable to match the location of your Java installation.  
                                                   .  
  
### Setup  

- Create an account at [Mastercard Developers](https://developer.mastercard.com/account/sign-up).  
- Create a new project and add `Mastercard Cross-Border Services` API to your project.   
- Configure project and download signing key. It will download the zip file.  
- Select .p12 file from zip and copy it to `src\main\resources` in the project folder.
- Generate client certificate(.PKCS12 or .PFX file) to establish mTLS connectivity. Contact customer support for more details.
- Open `[project folder]\src\main\resources\mastercard-api.properties` and configure below parameters.
    
    >**mastercard.api.environment.sandbox.endPoint URL for uk = https://sandbox.api.xbs.mastercard.uk/**, its a static field, will be used as a host to make API calls.
    >**mastercard.api.environment.sandbox.endPoint URL for eu = https://sandbox.api.xbs.mastercard.eu/**, its a static field, will be used as a host to make API calls.

    >**mastercard.api.environment.sandbox.partnerId=PSD2-OI-Payment**, this refers to partnerId of sandbox environment. This is a static field and need not be changed. If you wish, you can change it with your preferred partnerId.
    
    **Below properties will be required for authentication of API calls.**
    
    >**mastercard.api.authentication.keystore.keyFile=**, this refers to .p12 file found in the signing key. Please place .p12 file at src\main\resources in the project folder and add classpath for .p12 file.
    
    >**mastercard.api.authentication.consumerKey=**, this refers to your consumer key. Copy it from "Keys" section on your project page in [Mastercard Developers](https://developer.mastercard.com/dashboard)
      
    >**mastercard.api.authentication.keystore.keyalias=keyalias**, this is the default value of key alias. If it is modified, use the updated one from keys section in [Mastercard Developers](https://developer.mastercard.com/dashboard).
    
    >**mastercard.api.authentication.keystore.password=keystorepassword**, this is the default value of key alias. If it is modified, use the updated one from keys section in [Mastercard Developers](https://developer.mastercard.com/dashboard).
    
    **OPTIONAL CONFIGURATION**
    
    **Below properties will be required to encrypt / decrypt the request and response payloads. Contact APISupport@mastercard.com to get required keys and files for the same**
      
    >**mastercard.api.environment.runWithEncryptedPayload=false**, setting this field to true, will encrypt the request payload before sending the request. It will also return encrypted response.
    
    >**mastercard.api.encryption.certificateFile=**, this is the path to certificate (.crt) file. Add classpath for .crt file, after placing it at src\main\resources in the project folder.
    
    >**mastercard.api.encryption.fingerPrint=**, this is the encryption key, required to encrypt a request.
       
    >**mastercard.api.decryption.keyFile=**, this is the .key file, required to decrypt a request. Add classpath for .key file, after placing it at src\main\resources in the project folder.
 
 
### Build and Run   
`Using IDE`

 - Open reference application in IDE and dependencies will be downloaded automatically. Open the maven window,
	 -  Select MasterCard Cross-Border Services - Reference App
	 -  Select Life cycle 
	 -  Run clean and install

### Executing the use cases  
  
**Executing the use-cases in IDE**

- Go to [project folder](\src\test\java\com\mastercard\crossborder\api), Open any .java file, ex. QuotesAPITest.java.  
- Right click on any method in the class and run.  
- Use cases will execute as per partnerId defined in mastercard-api.properties.
>**Note: To change the input passed to APIs, modify values in CrossBorderAPITestHelper.java**  

### Use cases
Main use cases in cross-border APIs are Quote, Payment, Retrieve payment and Cancel payment. Below are few variations. 
 
A] Request a quote:
-  [Request a quote for Uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-quotes-api/):
-  [Request a quote for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-quotes-api/):

   Below are the different flavours available for a quote transaction:

> Case 1:  **FORWARD QUOTE WITH FEES INCLUDED** 
- Originating institute (OI) can make a forward quote request.  
- In case of forward quote with fees included, OI wants to specify the Sending amount and currency for a payment. Quotes response will provide the amount that will be received by beneficiary. 
e.g. If OI wants to send 100$, then amount received by beneficiary will be conversion of (100 $ - (fees)) in receiver currency.      
- Refer to #Usecase - 1 in [QuotesAPITest.java](./src\test\java\com\mastercard\crossborder\api\QuotesAPITest.java) for details.  

> Case 2:  **FORWARD QUOTE WITH FEES NOT INCLUDED**
- Originating institute (OI) can make a forward quote request with fees not included.  
- In case of forward quote with fees not included, OI will to specify the Sending amount and currency for a payment. Fees collected will be on top of what was requested to be paid. So OI will pay the fees but on top of initial amount.
- e.g. If OI wants to transfer 100$ to beneficiary then he will be asked to pay (100 $ + fees) and beneficiary will receive conversion of 100$ in receiving currency. 
- Refer to #Use case - 2 in [QuotesAPITest.java](./src\test\java\com\mastercard\crossborder\api\QuotesAPITest.java) for details.  

> Case 3:  **REVERSE QUOTE**
- Originating institute (OI) can initiate the request for reverse quote.  
- In this case of reverse quote, OI needs to ensure the beneficiary receives a specific amount in beneficiary currency. Quotes response will provide the amount to be transferred by OI.
e.g. If OI specifies beneficiary should receive 1000 INR(assuming beneficiary's currency is INR) as a fixed amount, OI will be asked to pay conversion of 1000 INR + fees as a quotes response.
- Refer to #Usecase - 3 in [QuotesAPITest.java](./src\test\java\com\mastercard\crossborder\api\QuotesAPITest.java) for details.
    
> Case 4:  **QUOTES REQUEST WITH ENCRYPTION**
- Originating institute (OI) can initiate the request for a quote.  
- This can be any type of quote, just that quotes request payload will be sent in encrypted form if property 'runWithEncryptedPayload' is set to True.
- Quotes response will also be in encrypted form.
- Refer to #Usecase - 4 in [QuotesAPITest.java](./src\test\java\com\mastercard\crossborder\api\QuotesAPITest.java) for details.

> Case 5:  **QUOTES REQUEST IN JSON FORMAT**
- Originating institute (OI) can initiate the request for reverse quote in Json format.  
- In this case of reverse quote, OI needs to ensure the beneficiary receives a specific amount in beneficiary currency. Quotes response will provide the amount to be transferred by OI.
e.g. If OI specifies beneficiary should receive 1000 INR(assuming beneficiary's currency is INR) as a fixed amount, OI will be asked to pay conversion of 1000 INR + fees as a quotes response.
- Refer to #Usecase - 5 in [QuotesAPITest.java](./src\test\java\com\mastercard\crossborder\api\QuotesAPITest.java) for details.
    
    
> Case 6:  **QUOTES REQUEST WITH ENCRYPTION IN JSON FORMAT**
- Originating institute (OI) can initiate the request for a quote in Json format.  
- This can be any type of quote, just that quotes request payload will be sent in encrypted form if property 'runWithEncryptedPayload' is set to True.
- Quotes response will also be in encrypted form.
- Refer to #Usecase - 6 in [QuotesAPITest.java](./src\test\java\com\mastercard\crossborder\api\QuotesAPITest.java) for details.

B] Make payment:   
-  [Make payment for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-payment-api/): 
-  [Make payment for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-payment-api/): 

   Payment can be made in two ways. Make payment using quote, One shot payment (payment without quotes).

> Case 1: **PAYMENT WITH FORWARD QUOTE**
- OI wants to initiate quotes request to make a payment with quote. A quote can be forward or reverse.
- Refer Quotes Use cases 1,2 or 3 to make forward or reverse quote request.
- Proposal Id from the quotes response should be used make payment request
- Please refer to #Usecase - 1 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details. 

> Case 2: **FORWARD PAYMENT WITHOUT QUOTE WITH NOT FEES INCLUDED FOR PERSON TO BUSINESS PAYMENT TYPE**
- OI wants to make a direct payment without making separate a quote request .
- For one shot payment, quotes related information need to be passed in payment request itself.
- In this case payment request consists of forward quote without fees included and P2B payment type information.  
- Please refer to #Usecase - 2 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

> Case 3: **FORWARD PAYMENT WITHOUT QUOTE WITH FEES INCLUDED FOR PERSON TO PERSON PAYMENT TYPE**
- OI wants to make a direct payment without making separate a quote request .
- For one shot payment, quotes related information need to be passed in payment request itself.
- In this case payment request consists of forward quote with fees included and P2P payment type information.  
- Please refer to #Usecase - 3 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

> Case 4: **REVERSE PAYMENT WITHOUT QUOTE FOR BUSINESS TO PERSON PAYMENT TYPE**
- OI wants to make a direct payment without making separate a quote request .
- For one shot payment, quotes related information need to be passed in payment request itself.
- In this case payment request consists of reverse quote and B2P payment type information.  
- Please refer to #Usecase - 4 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

> Case 5: **FORWARD PAYMENT WITHOUT QUOTE FOR BUSINESS TO BUSINESS PAYMENT TYPE**
- OI wants to make a direct payment without making separate a quote request .
- For one shot payment, quotes related information need to be passed in payment request itself.
- In this case payment request consists of forward quote with fees included and B2B payment type information.  
- Please refer to #Usecase - 5 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

> Case 6: **FORWARD PAYMENT WITHOUT QUOTE FOR GOVERNMENT TO PERSON PAYMENT TYPE**
- OI wants to make a direct payment without making separate a quote request .
- For one shot payment, quotes related information need to be passed in payment request itself.
- In this case payment request consists of forward quote with fees included and G2P payment type information.  
- Please refer to #Usecase - 6 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

> Case 7: **ERROR HANDLING**
- OI wants to make a direct payment with any of the above types.
- Whereas the payment can fail for various reasons.
- This use case just shows one of the example of such failure.
- You can refer [Error Codes](https://developer.mastercard.com/cross-border-services/documentation/api-ref/error-codes/) to understand different types of errors. 
- Please refer to #Usecase - 7 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

> Case 8:  **PAYMENT WITH ENCRYPTION SUPPORTED**
- Originating institute (OI) can initiate any of the above type of payment with encryption.  
- In this case request payload of payment will be sent in encrypted form if property 'runWithEncryptedPayload' is set to True.
- Payment response will also be in encrypted form.
- Refer to #Usecase - 8 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

> Case 9:  **PAYMENT WITH FORWARD QUOTE IN JSON FORMAT**
- OI wants to initiate quotes request to make a payment with quote in Json format. 
- In this example forward quote with fees included is used.
- Proposal Id from the quotes response should be used make payment request
- Please refer to #Usecase - 9 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details. 
      

> Case 10:  **ERROR HANDLING IN JSON FORMAT**
- OI wants to make a direct payment with any of the above types.
- Whereas the payment can fail for various reasons.
- This use case just shows one of the example of such failure where media type is Json.
- You can refer [Error Codes](https://developer.mastercard.com/cross-border-services/documentation/api-ref/error-codes/) to understand different types of errors. 
- Please refer to #Usecase - 10 in [RemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java) for details.

C]  Retrieve payment:
-  [Retrieve payment for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-retrieve-payment-api/): 
-  [Retrieve payment for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-retrieve-payment-api/): 

   Status of a payment can be seen by retrieve payment API calls. There are two ways of knowing the details of payment,
Get payment by ID and Get payment by reference.

> Case 1: **RETRIEVE PAYMENT BY MASTERCARD PROVIDED ID**
- OI can call this method to know the status of payment.
- Payment Id returned while making a payment can be used to get the payment details.  
- Refer to #Usecase - 1 in [GetRemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\GetRemittanceAPITest.java) for details.

> Case 2: **RETRIEVE PAYMENT BY OI PROVIDED TRANSACTION REFERENCE ID**
- OI can call this method to know the status of payment.
- transaction_reference passed as input to payment can be used to get the payment details.  
- Refer to #Usecase - 2 in [GetRemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\GetRemittanceAPITest.java) for details.

> Case 3: **RETRIEVE PAYMENT IN ENCRYPTED FORM**
- OI can call this method to know the status of payment, made with or without quote.
- transaction_reference or payment ID returned while making a payment can be used to get the payment details.  
- In this case, request payload is empty but the response payload will be encrypted.
- Refer to #Usecase - 3 in [GetRemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\GetRemittanceAPITest.java) for details.

> Case 4: **RETRIEVE PAYMENT BY OI PROVIDED TRANSACTION REFERENCE ID IN JSON FORMAT**
- OI can call this method to know the status of payment in Json format.
- transaction_reference passed as input to payment can be used to get the payment details.  
- Refer to #Usecase - 4 in [GetRemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\GetRemittanceAPITest.java) for details.

D] Cancel payment:
-  [Cancel payment for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-cancel-payment-api/):
-  [Cancel payment for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-cancel-payment-api/):

   This facility of cancelling a payment is available only for some mobile money providers and cash-out. It is not available for banks account channels.
Payment can be cancelled if the payment is in 'Pending' status.
> Case 1: **CANCEL A PAYMENT**
- OI can cancel a payment by making Cancel payment API call. 
- You can use the payment Id returned while making a payment. 
- Refer to #Usecase - 1 in [CancelRemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\CancelRemittanceAPITest.java) for details.

> Case 2: **CANCEL A PAYMENT WITH ENCRYPTION**
- OI can cancel a payment by making Cancel payment API call in encrypted form. 
- Payment can be cancelled only if the status of payment is in ‘Pending’ status, payment Id can be returned while making a payment can be used here.
- Request and response payload will be encrypted.
- Refer to #Usecase - 2 in [CancelRemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\CancelRemittanceAPITest.java) for details.

> Case 3: **CANCEL A PAYMENT IN JSON FORMAT**
- OI can cancel a payment by making Cancel payment API call in Json format. 
- You can use the payment Id returned while making a payment. 
- Refer to #Usecase - 3 in [CancelRemittanceAPITest.java](./src\test\java\com\mastercard\crossborder\api\CancelRemittanceAPITest.java) for details.

E] Carded Rate API:
-  [Carded Rate API for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-carded-rate-api/):
-  [Carded Rate API for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-carded-rate-api/):

   Carded Rate is offered as an opt-in functionality to obtain the FX rates for the currency pairs that you, as the originating institution (OI) support, for a valid period of time.

- [FX Rate Pull](https://developer.mastercard.com/cross-border-services/documentation/api-ref/carded-rate-api/#fx-rate-pull):
The FX Rate Pull API will require you to create a scheduler that will call this API based on the refresh times per currency pair provided by Mastercard Cross-Border services.
This API supports only JSON.
> Case 1: **PULL CARDED RATE**
- OI can call this method to get FX rates
- For FX Rates retrieval, we need to pass partner-id in the request URL itself
- Please refer to #Usecase - 1 in [PullCardedAPITest.java](./src\test\java\com\mastercard\crossborder\api\PullCardedAPITest.java) for details.

> Case 2: **PULL CARDED RATE WITH ENCRYPTION**
- OI can call this method to get FX rates
- For FX Rates retrieval, we need to pass partner-id in the request URL itself.
- In this case, request payload is empty but the response payload will be encrypted.
- Please refer to #Usecase - 2 in [PullCardedAPITest.java](./src\test\java\com\mastercard\crossborder\api\PullCardedAPITest.java) for details.

### Authentication details 

Cross border APIs support OAuth2.0 request token based authentication for EU and UK domains. 

OAUTH2_REQUEST_TOKEN grant type is a special implementation by Mastercard. It is an authentication and authorization method implemented to allow consumer 
application to consume Cross-Border Service APIs and it is one-legged auth implementation. It means your API request must contain a signed JWT token 
(using token specification) as ‘Authorization’ header for authentication.

### Implementation details for cross-border APIs

To develop a client application using cross border APIs, refer below documentation. All the cross-border APIs are REST APIs that support both XML and JSON as a payload.   

- [Request a quote for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-quotes-api/) (HTTP POST)
- [Request a quote for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-quotes-api/) (HTTP POST)

- [Make payment for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-payment-api/) (HTTP POST)
- [Make payment for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-payment-api/) (HTTP POST)


- [Retrieve payment for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-retrieve-payment-api/) (HTTP GET)
- [Retrieve payment for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-retrieve-payment-api/) (HTTP GET)

- [Cancel payment for uk](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-uk-cancel-payment-api/) (HTTP POST)
- [Cancel payment for eu](https://developer.mastercard.com/cross-border-services/documentation/api-ref/psd2-eu-cancel-payment-api/) (HTTP POST)


To understand implementation details, you can refer to makePayment() in [RemittanceAPI.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java).

### Implementation details for cross-border APIs that support encryption

Cross border APIs support JWE encryption. To develop an application that uses cross border APIs with encryption, below steps are to be followed.

**Step1**: HttpHeaders should have the parameter "x-encrypted" = "true".

**Step2**: You can encrypt the request payload by calling EncryptionUtils.jweEncrypt(). Parameters required for encryption are fingerPrint key and certificate (.crt) file. For more information on encryption keys, contact mastercard support team.

**Step3**: Wrap encrypted input in below format.
           
    For XML              
    <?xml version="1.0" encoding="UTF-8" ?>
    <encrypted_payload>
        <data>encryptedPayloadAsAString</data>
    </encrypted_payload>   
    
    For JSON            
    {
      "encrypted_payload": { "data": "encryptedPayloadAsAString" }
    }      

**Step4**: Make an API call Pass with this encrypted request entity and you will get the encrypted response.

**Step5**: You can decrypt the response by calling EncryptionUtils.jweDecrypt(). Parameters required for decryption are key (.key) file. For more information on decryption keys, contact mastercard support team. There are multiple ways of decrypting a response. Reference implementation talks about only one type of decryption. Please refer https://developer.mastercard.com/cross-border-services/documentation/api-ref/encryption/ for more details.

To understand implementation details of encryption and decryption, you can refer to makePaymentWithEncryption() in [RemittanceAPI.java](./src\test\java\com\mastercard\crossborder\api\RemittanceAPITest.java).





