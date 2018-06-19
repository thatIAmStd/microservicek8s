from message.api import MessageService
from thrift.transport import Tsocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryprotocol
from thrift.server import TServer

import smtplib
from email.mime.text import MIMEText
from email.header import Header

sender = 'xxx@163.com'
authCode = 'xxxx'


class MessageServiceHandler:

    def sendMobileMessage(self, mobile, message):
        print ("sendMobileMessage,mobile:" + mobile + ",message:" + message)
        return True

    def sendEmailMessage(self, email, message):
        print ("sendEmailMessage,email:" + email + ",message:" + message)
        messageObj = MIMEText(message, "plain", "utf-8")
        messageObj['From'] = sender
        messageObj['To'] = email
        messageObj['Subject'] = Header('k8s服务编排测试邮件', "utf-8")
        #TODO python 异常自动提示
        try:
            smtpObj = smtplib.SMTP('smpt.163.com')
            smtpObj.login(sender, authCode)
            smtpObj.sendmail(sender, [email], messageObj.as_string())
            print("send mail success")
            return True
        except smtplib.SMTPException, ex:
            print ("send mail failed!")
            print (ex)
            return False


if __name__ == '__main__':
    handler = MessageServiceHandler()
    processor = MessageService.Processor(handler)
    # 监听端口
    transport = Tsocket.TServerSocket("localhost", "9090")
    # 传输方式
    tfactory = TTransport.TFramedTransportFactory()
    # 传输协议
    pfactory = TBinaryprotocol.TBinaryProtocolFactory()
    # 创建服务
    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print("python thrift server start")
    server.serve()
    print("python thrift server exit")
