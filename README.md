# Web server programmed in Java
  - Name: Adam Hudson
  - Class: CSE 4344 Computer Networks

## Instructions on how to run code:
I developed my code on linux with Oracle Java(TM) SE Runtime Environment (build 1.8.0_144-b01). I have included two html files in the root directory of the folder to test the code with. Index.html is the good web page to request and index1.html has a 301 error in the meta data.

## The way my code is run is like so:
```
$ javac web/server/*.java 
$ java web.server.WebServer
```
- After the code is running, simply open your web browser and type in http://localhost:6789/index.html to see the web page.

## Known bugs and limitations:
I know that when I read my example html file that contains redirect content, my HttpRequest class cannot read in the file and therefore throws an error 404 page not found.

## Assumptions made:
I set my buffer size to be 1024 bytes so that I would not run myself out of memory and I would still have plenty of memory to write my requests. 
