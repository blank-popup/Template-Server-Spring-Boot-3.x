# Introduction
System manage attendance of members.

# Summary
## Server
Spring Boot using REST API  
Receiving information of card and phone  
Managing received information including time

## Client(Android)
Android phone using NFC  
Reading information of card  
Sending information of card and phone

## (unimplemented) Client(Web) 
React  
Web page for administrator

# Details
## System
RollBook use credit card and android phone. Via NFC android phone read card. Card is unique and we can identify the owner of card. Android phone is unique and we can identify when and where the owner of card is touched.
1. Member touch card to Android phone
2. Android phone recognize which card is touched using NFC
3. Android phone send information to server
4. Server receive information(which card is used, which phone is used and when it is used)
5. Server manage information and send it according to web's request
6. (unimplemented) Web display server's response 


## Server
Server communicate client via REST API and manage attendance totally. It receive information of card and android phone. And it store information in database. Server read this information from database. And it send information according to client's request.

Android only send who and where touch card to server. Server can know who, when and where is attending, save and read this information.

Web can send request to display result of attendance. Administrator can know attendance of members. For example, who, when and where is attending and getting off.

## Client(Android)
Client(Android) is Using NFC. Android cliet only read and send information(which card is used, which phone is used). And it display result of requesting.

## (unimplemented)Client(Web)
Web is page for administrator. Via web, administrator manage system.
