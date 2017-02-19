# walmart-test


# In order to execute

Run mvn clean install tomcat7:run

Use <strong>http://localhost:8401/ws/</strong>to access the Web service


# map/sample
<pre>
HTTP request:<br/>
GET /ws/map/sample HTTP/1.1
Host: localhost:8401
Content-Type: application/json
Cache-Control: no-cache
</pre>
Return
<pre>
{"name":"example-map","routes":[{"origin":"A","destination":"B","distance":10},{"origin":"B","destination":"D","distance":15},{"origin":"A","destination":"C","distance":20},{"origin":"C","destination":"D","distance":30},{"origin":"B","destination":"E","distance":50},{"origin":"D","destination":"E","distance":30}]}

</pre>

# map/save
<pre>
JQuery request:<br/>
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
</pre>

# map/search/{name}

<pre>
HTTP request:<br/>
GET /ws/map/search/map-004 HTTP/1.1
Host: localhost:8401
Content-Type: application/json
Cache-Control: no-cache
</pre><pre>
{"name":"map-004","routes":[{"origin":"A","destination":"B","distance":10},{"origin":"B","destination":"D","distance":15},{"origin":"A","destination":"C","distance":20},{"origin":"C","destination":"D","distance":30},{"origin":"B","destination":"E","distance":50},{"origin":"D","destination":"E","distance":30}]}
</pre>

# map/calc/{name}/{autonomy}/{gasprice}/{origin}-{destination}

<pre>
HTTP request:<br>
GET /ws/map/calc/map-003/10/2.5/A-D HTTP/1.1
Host: localhost:8401
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: 0968b9d9-a570-060a-6766-36b435cad974
</pre>

# map/calc/{autonomy}/{gasprice}/{origin}-{destination}
<pre>
JQuery request:</br>
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
<pre>