# walmart-test

Test built upon Java EE platform. 
<br/>
Web service RESTful.
<ul>
	<li>Tools: Eclipse, Maven and MongoDB;</li>
	<li>Frameworks: Spring MVC, MongoJack, Swagger UI and JGraphT</li>
	<li>Version Control: git https://github.com/dsasoft/walmart-test</li>
</ul>

# Instructions

It's possible to persist and retrieve a Map, through the methods available, but also it's possible send a Map inside HTTP POST Body of request, and get the result right away.

In order to execute, use Maven command line: 
<code>mvn clean install tomcat7:run</code>
<br/>
Use <strong>http://localhost:8401/ws/</strong> URL to access the Web service<br/><br/>

In order to debug the code, use a Maven remote debug option.<br/>

Run	<code>mvnDebug clean install tomcat7:run</code><br/>

Then use the steps, to setup your remote debug, for example in Eclipse IDE, go to Menu:
<br/>Run / Debug Configuration...
<ul>
	<li>Project: {ProjectName}</li> 
	<li>Connection Type: Standard (Socket Attach)</li>
	<li>Host: localhost</li>
	<li>Port: 8000</li>
</ul>

![Eclipse setup](https://github.com/dsasoft/walmart-test/blob/master/ScreenShot.Eclipse.RemoteDebug.png)

# API documentation

It's available an API documentation on http://localhost:8401/ws/swagger-ui.html

# map/sample

Display a Map example

<pre>
HTTP request:<br/>
GET /ws/map/sample HTTP/1.1
Host: localhost:8401
Content-Type: application/json
Cache-Control: no-cache
</pre>
Return
<pre>
<code>
{
   "name":"example-map",
   "routes":[
      {
         "origin":"A",
         "destination":"B",
         "distance":10
      },
      {
         "origin":"B",
         "destination":"D",
         "distance":15
      },
      {
         "origin":"A",
         "destination":"C",
         "distance":20
      },
      {
         "origin":"C",
         "destination":"D",
         "distance":30
      },
      {
         "origin":"B",
         "destination":"E",
         "distance":50
      },
      {
         "origin":"D",
         "destination":"E",
         "distance":30
      }
   ]
}
</code>
</pre>
# map/save

Save new Map

JQuery request:<br/>
<code>
var settings = {
  "async": true,
  "crossDomain": true,
  "url": "http://localhost:8401/ws/map/save/",
  "method": "POST",
  "headers": {
    "content-type": "application/json",
    "cache-control": "no-cache",
  },
  "processData": false,
  "data": "{\n\t\"name\":\"map-004\",\n    \"routes\": [\n        {\n            \"origin\": \"A\",\n            \"destination\": \"B\",\n            \"distance\": 10\n        },\n        {\n            \"origin\": \"B\",\n            \"destination\": \"D\",\n            \"distance\": 15\n        },\n        {\n            \"origin\": \"A\",\n            \"destination\": \"C\",\n            \"distance\": 20\n        },\n        {\n            \"origin\": \"C\",\n            \"destination\": \"D\",\n            \"distance\": 30\n        },\n        {\n            \"origin\": \"B\",\n            \"destination\": \"E\",\n            \"distance\": 50\n        },\n        {\n            \"origin\": \"D\",\n            \"destination\": \"E\",\n            \"distance\": 30\n        }\n    ]\n}"
}
$.ajax(settings).done(function (response) {
  console.log(response);
});
</code>

# map/search/{name}

Search for Map by given name

<pre>
HTTP request:<br/>
GET /ws/map/search/map-004 HTTP/1.1
Host: localhost:8401
Content-Type: application/json
Cache-Control: no-cache
</pre>
Return
<code>
{
   "name":"map-004",
   "routes":[
      {
         "origin":"A",
         "destination":"B",
         "distance":10
      },
      {
         "origin":"B",
         "destination":"D",
         "distance":15
      },
      {
         "origin":"A",
         "destination":"C",
         "distance":20
      },
      {
         "origin":"C",
         "destination":"D",
         "distance":30
      },
      {
         "origin":"B",
         "destination":"E",
         "distance":50
      },
      {
         "origin":"D",
         "destination":"E",
         "distance":30
      }
   ]
}
</code>

# map/calc/{name}/{autonomy}/{gasprice}/{origin}-{destination}

Measure the shortest distance between Origin and Destiny inside already saved Map, and cost based on also given Truck's autonomy and Gas price.
<br/>
<br/>
Following the premise that the Map's values is only one way. For example: The distance between A to B is 15, the reverse (B to A) is not necessarily the same distance. <br/> 
It uses JGraphT Dijkstra algorithm implementation.
<pre>
HTTP request:<br>
GET /ws/map/calc/map-003/10/2.5/A-D HTTP/1.1
Host: localhost:8401
Content-Type: application/json
Cache-Control: no-cache
</pre>

# map/calc/{autonomy}/{gasprice}/{origin}-{destination}

Measure the shortest distance between Origin and Destiny inside a given Map inside HTTP Request body and cost based on also given Truck's autonomy and Gas price. 
<br/>
<br/>
Following the premise that the Map's values is only one way. For example: The distance between A to B is 15, the reverse (B to A) is not necessarily the same distance. 
<br/>
It uses JGraphT Dijkstra algorithm implementation.

JQuery request:</br>
<code>
var settings = {
  "async": true,
  "crossDomain": true,
  "url": "http://localhost:8401/ws/map/calc/10/2.5/A-D",
  "method": "POST",
  "headers": {
    "content-type": "application/json",
    "cache-control": "no-cache",
  },
  "processData": false,
  "data": "{\"name\":\"map-001\",\"routes\":[{\"origin\":\"A\",\"destination\":\"B\",\"distance\":1234},{\"origin\":\"B\",\"destination\":\"D\",\"distance\":15},{\"origin\":\"A\",\"destination\":\"C\",\"distance\":20},{\"origin\":\"C\",\"destination\":\"D\",\"distance\":30},{\"origin\":\"B\",\"destination\":\"E\",\"distance\":50},{\"origin\":\"D\",\"destination\":\"E\",\"distance\":30}]}"
}
$.ajax(settings).done(function (response) {
  console.log(response);
});
</code>
