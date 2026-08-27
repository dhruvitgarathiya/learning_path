server side tech , to handle the client request- process the request and generate dynamic response

client - simple browser

life cycle of servlet - 

1. loading and instantiation

when server is started ,servlet class is loaded in th memory & servlet object is created

2. intialization - init()

servlet object will be intialized by invoking inti() method

3. request handling - service()

it will handle or serve the client request , in this phase service() mathod will be invoked

4. destroy - destroy()

when the server is shut down destroy() method will be executed & servlet object will be deleted.

### 

which servlet i want to execute - decided by web.xml file - by client's request detail it maps the servlet


web.xml : also known as "Deployment descriptor"

in latest versions it is not necessary to use this file.
annoations - @webServlet() by using this we can do work of web.xml without making the file

### 

get and post - http methods

get - url through transfer data - security not maintained - visible data - fast

post - through http message - slow - high secturity

doGet() MethodPurpose: Used to fetch or retrieve data from the server.Data Transmission: Appends form data or parameters directly to the URL string visible in the browser address bar.Security & Limits: Less secure because data is exposed; limited in size (usually up to 2048 characters) and restricted to ASCII character data.Use Cases: Loading web pages, searching records, or querying data.

doPost() MethodPurpose: Used to send, submit, or update data on the server.Data Transmission: Sends data securely hidden inside the HTTP request body rather than the URL.Security & Limits: More secure for sensitive info; supports large amounts of data and binary file uploads.Use Cases: Submitting registration forms, processing logins, or modifying database records.

### 

sendRedirect  method -  from my app to another app redirection 

httpservlet response reference is being used for this

requestDispatcher - never change url , internally travelling in application

### 

multiple time using request for getting values is not a good way for heavy data request

that's where we use session - at time of login we set the values 


HttpServletRequest -> httpsession hs
hs.getsession

hs.setAttribute

hs.removeAttribute

hs.invalidate


# jsp

servlet - server side technology 
by default java page
we embedd html code

jsp - server side scripting technology 
by default html page
we embadd java code



## lifecylce of jsp

1. translation -

jsp -> servlet

2. compilation -

servlet -> .class

3. loading and instantion

.class file is loaded on server

& object is created

4. intialization


(jspInit())

5. request handling

(jspService()

6. destory

(jspDestroy())


## jsp tags

* scriptig tags ( by which we can emabdd java code)
* durectuve tage (jsp container instruction is provided how to process jsp )
* action tag (speicific task perform )


