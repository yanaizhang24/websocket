package cn.yanf.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Administrator on 2016/11/15.
 */
@WebService
public class HelloService {
    @WebMethod
    public String sayHello(String message){
        return "Hello ," + message;
    }

    public static void main(String[] args) {
        //create and publish an endPoint
        HelloService hello = new HelloService();
        Endpoint endPoint = Endpoint.publish("http://localhost:8080/helloService", hello);
    }
}
