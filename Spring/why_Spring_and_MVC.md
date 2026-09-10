# before spring we were using sevlets

in which we have to
* create object manually using new in all places
* we have to handle connection pooling by ourselves 
* dependency have manage manually by ourselves like making data access object class for servlet 1 and servlet 2 to intract with each other using redirec and forward
* manually create the web.xml file and add dependecies init
* we have to handle transaction by ourselves


# how spring solves it

* provide the ioc and di - to not to create object manually container handles it
* raw jdbc try catch resources everywhere , spring handles it with jdbc template
* in servlet we have all the code scattered like logging , processing etc - spring works with AOP aspect oriented programming in that like we write concern that (start - trascation processs - commit ) and we can apply this concern everywhere
* integration plan - need to talk to db, manage queue this all need different execuion and library , own boilerplate but spring use consistent programming method in this same di style , same config style so easy to work with

# why not any other framework is not able to replace it till now

* bigger umbrella spring have expanded it size with spring data, spring boot, spring ai , spring cloud etc
* constant invension and modificationa and investment
* big strenght of spring is that we just have to wrtie buiness logic in pojo class , not to implement or extend the spring class or interface. this is big changee compare to older frameworks. 


## Spring mvc - 
module inside spring core built for web app, handle http request response cycle using mvc pattern

spring core - ioc/di , generic work for any app ( web , batch , windows) -- spring mvc web layer built on the spring core. all spring functionalities are there but web functionality added

