package com.hydeng.user.thrift;

import com.hydeng.thrift.user.UserService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-24
 */
@Configuration
public class ThriftServer {

    @Value("${service.port}")
    private Integer servicePort;

    @Autowired
    private UserService.Iface userService;

    @PostConstruct
    public void startThriftServer(){
        TProcessor processor = new UserService.Processor<>(userService);
        //连接方式
        TNonblockingServerSocket socket = null;
        try {
            socket = new TNonblockingServerSocket(servicePort);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        TNonblockingServer.Args args = new TNonblockingServer.Args(socket);
        args.processor(processor);
        //传输方式 TODO 记录一下日志
        args.transportFactory(new TFramedTransport.Factory());
        //传输协议
        args.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new TNonblockingServer(args);
        server.serve();
    }

}
