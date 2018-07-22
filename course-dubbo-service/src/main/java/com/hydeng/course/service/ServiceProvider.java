package com.hydeng.course.service;

import com.hydeng.thrift.user.UserService;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-26
 */
@Component
public class ServiceProvider {

    @Value("${thrift.user.ip}")
    private String thriftUserIp;

    @Value("${thrift.user.port}")
    private int thriftUserPort;

    enum ServiceType{
        USER,
        MESSAGE
    }


    public UserService.Client getUserService(){
        return getService(thriftUserIp,thriftUserPort, ServiceType.USER);
    }



    public <T> T getService(String ip,int port,ServiceType serviceType){
        TSocket socket = new TSocket(ip,port,10000);
        TTransport tTransport = new TFramedTransport(socket);
        try {
            tTransport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            return null;
        }
        TProtocol protocol = new TBinaryProtocol(tTransport);

        TServiceClient result = null;

        switch (serviceType){
            case USER:
                result = new UserService.Client(protocol);
                break;
        }
        return (T)result;
    }

}
