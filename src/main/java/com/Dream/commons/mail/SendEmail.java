package com.Dream.commons.mail;

import org.apache.log4j.Logger;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by wly on 2018/3/7.
 */
public class SendEmail {
    private final static Logger log = Logger.getLogger( SendEmail.class);

    private static String SendEmail = "3033350874@qq.com";

    private static String password = "exivieguiraidhdf";

    private static String smtpHost = "smtp.qq.com";

    private static String port = "465";

    /**
     * @param email 收件人邮箱
     * @param validateCode 验证代码
     * @param personal 收件人名字
     * @throws Exception
     */
    public static void sendMail(String email, String validateCode, String personal) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocal","smtp");
        props.setProperty("mail.smtp.host",smtpHost);
        props.setProperty("mail.smtp.auth","true");
        //SSL安全认证
        props.setProperty("mail.smtp.port",port);
        props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback","false");
        props.setProperty("mail.smtp.socketFactory.port",port);

        //创建会话对象，用于和邮件服务器进行交互
        Session session = Session.getInstance(props);
        session.setDebug(true);

        //3.创建一封邮件
        MimeMessage message = createMimeMessage(session, SendEmail, email, validateCode, personal);
        //用session获取传输对象
        Transport transport = session.getTransport();
        //连接
        transport.connect(SendEmail, password);
        //发送邮件，getAllRecipients获取所有的收件人
        transport.sendMessage(message, message.getAllRecipients());
        //关闭连接
        transport.close();

    }
    private static MimeMessage createMimeMessage(Session session, String sendEmail, String receiveEmail, String code, String personal) throws Exception{
        //创建一封邮件
        MimeMessage message = new MimeMessage(session);
        //From：发件人
        message.setFrom(new InternetAddress(sendEmail, "易班综测平台","UTF-8"));
        //To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveEmail, personal, "UTF-8"));
        //主题
        message.setSubject("易班综测平台激活链接", "UTF-8");
        message.setContent( "<a href=\"http://localhost:8080/activate?email="+receiveEmail+"&validateCode="+ code+"\" target=\"_blank\">请于24小时内点击激活</a>","text/html;charset=gb2312");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
}
