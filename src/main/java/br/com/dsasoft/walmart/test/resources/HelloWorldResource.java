package br.com.dsasoft.walmart.test.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldResource {
	
	@RequestMapping(value = "/hello/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<String> helloWorld(@PathVariable("name") String name){
		StringBuilder sb = new StringBuilder("Hello ");
		sb.append(name);
		sb.append("!");
		return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
	}

}
