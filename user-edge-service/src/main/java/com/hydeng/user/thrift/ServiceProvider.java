package com.hydeng.user.thrift;

import com.hydeng.thrift.message.MessageService;
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

    @Value("${thrift.message.ip}")
    private String thriftMessageIp;

    @Value("${thrift.message.port}")
    private int thriftMessagePort;

    enum ServiceType{
        USER,
        MESSAGE
    }


    public UserService.Client getUserService(){
        return getService(thriftUserIp,thriftUserPort,ServiceType.USER);
    }

    public MessageService.Client getMessageService(){
        return getService(thriftMessageIp,thriftMessagePort,ServiceType.MESSAGE);
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
            case MESSAGE:
                result = new MessageService.Client(protocol);
                break;

        }
        return (T)result;
    }

}
