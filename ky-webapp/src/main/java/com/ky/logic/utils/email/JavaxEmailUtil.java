package com.ky.logic.utils.email;


import com.ky.logic.model.info.MailInfo;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * javax.mail
 * <p/>
 * Created by yl on 2017/8/25.
 */
public class JavaxEmailUtil {

    /**
     * 465 端口 txt
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendTxtMailByPort465(MailInfo mailInfo) {
        try {
            System.out.println("testSendMail Begin-----------------=" + mailInfo.getToAddress());
            Message mailMessage = getMailMessage(mailInfo);

            // 设置发件人
            mailMessage.setFrom(new InternetAddress(mailInfo.getSenderAddress()));

            // 单个收件人
            //Address to = new InternetAddress(mailInfo.getToAddress().get(0));
            //msg.setRecipient(Message.RecipientType.TO, to);

            // 批量收件人
            to(mailMessage, mailInfo.getToAddress());

            // 标题
            mailMessage.setSubject(mailInfo.getSubject());

            // 当成纯文本内容
            mailMessage.setText(mailInfo.getContent());

            Transport.send(mailMessage);
            System.out.println(JavaxEmailUtil.class.getName() + "sendHtmlMailByPort465" + "EmailUtil ssl协议邮件发送打印" + mailMessage.toString());
        } catch (Exception e) {
            System.out.println("testSendMail End:" + "sslsend" + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 465 端口(Html格式，支持附件)
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendHtmlMailByPort465(MailInfo mailInfo) {
        System.out.println("testSendMail Begin-----------------=" + mailInfo.getToAddress());
        try {

            Message mailMessage = getMailMessage(mailInfo);

            // 设置发件人
            mailMessage.setFrom(new InternetAddress(mailInfo.getSenderAddress()));

            // 单个收件人
            //Address to = new InternetAddress(mailInfo.getToAddress().get(0));
            //msg.setRecipient(Message.RecipientType.TO, to);

            // 批量收件人
            to(mailMessage, mailInfo.getToAddress());

            // 标题
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());

            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();

            // MiniMultipart容器 添加 html内容
            multipartHtml(mainPart, mailInfo.getContent());

            // MiniMultipart容器 添加附件
            multipartAtts(mainPart, mailInfo.getAttachFile());

            // MiniMultipart容器
            mailMessage.setContent(mainPart);

            Transport.send(mailMessage);
            System.out.println(JavaxEmailUtil.class.getName() + "sendHtmlMailByPort465" + "EmailUtil ssl协议邮件发送打印" + mailMessage.toString());
        } catch (Exception e) {
            System.out.println("testSendMail End:" + "sslsend" + e.getMessage());
            return false;
        }

        return true;
    }


    /**
     * 25端口 txt格式
     *
     * @return void
     */
    public static void sendTxtEmailBy25(MailInfo mailInfo) {
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = getMailMessage(mailInfo);

            // 设置发件人
            mailMessage.setFrom(new InternetAddress(mailInfo.getSenderAddress()));

            // 批量收件人
            to(mailMessage, mailInfo.getToAddress());

            // 标题
            mailMessage.setSubject(mailInfo.getSubject());

            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());


            // 当成纯文本内容
            mailMessage.setText(mailInfo.getContent());

            // 发送邮件
            Transport.send(mailMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Message getMailMessage(MailInfo mailInfo) {

        Message mailMessage = null;
        if ("465".equals(mailInfo.getSmtpPort())) {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            // Get a Properties object
            Properties props = new Properties();
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.host", mailInfo.getSmtpServer());
            props.setProperty("mail.smtp.port", mailInfo.getSmtpPort());
            props.setProperty("mail.smtp.socketFactory.port", mailInfo.getSmtpPort());
            props.put("mail.smtp.auth", "true");

            final String username = mailInfo.getSenderAddress();
            final String password = mailInfo.getSenderPassword();
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            mailMessage = new MimeMessage(session);

        } else if ("25".equals(mailInfo.getSmtpPort())) {

            // 如果需要身份认证，则创建一个密码验证器
            if (mailInfo.isValidate()) {
                // 判断是否需要身份认证
                Properties props = new Properties();
                props.setProperty("mail.smtp.host", mailInfo.getSmtpServer());
                props.setProperty("mail.smtp.port", mailInfo.getSmtpPort());
                props.put("mail.smtp.auth", "true");

                MyAuthenticator authenticator = new MyAuthenticator(mailInfo.getSenderAddress(), mailInfo.getSenderPassword());
                Session session = Session.getInstance(props, authenticator);
                mailMessage = new MimeMessage(session);
            } else {
                Properties props = new Properties();
                //props.setProperty("mail.smtp.host", mailInfo.getSmtpServer());
                //props.setProperty("mail.smtp.port", mailInfo.getSmtpPort());
                //props.put("mail.smtp.auth", "false");
                Session session = Session.getInstance(props,null);
                mailMessage = new MimeMessage(session);
            }

        }


        return mailMessage;
    }


    // 批量收件人
    private static void to(Message mailMessage, List<String> toAddress) throws Exception {
        Address[] addresses = new Address[toAddress.size()];
        if (null != toAddress && toAddress.size() > 0) {
            for (int i = 0; i < toAddress.size(); i++) {
                Address to = new InternetAddress(toAddress.get(i));
                addresses[i] = to;
            }
        }
        mailMessage.setRecipients(Message.RecipientType.TO, addresses);
    }

    private static void multipartAtts(Multipart mainPart, List<File> attachFile) {
        for (File att : attachFile) {
            multipartAtt(mainPart, att);
        }
    }

    private static void multipartAtt(Multipart mainPart, File att) {
        try {
            // 存放邮件附件的MimeBodyPart
            MimeBodyPart attachment = new MimeBodyPart();
            // 根据附件文件创建文件数据源
            FileDataSource fds = new FileDataSource(att);
            attachment.setDataHandler(new DataHandler(fds));
            // 为附件设置文件名
            attachment.setFileName(MimeUtility.encodeWord(att.getName(), "GBK", null));
            mainPart.addBodyPart(attachment);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void multipartHtml(Multipart mainPart, String content) throws Exception {
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart html = new MimeBodyPart();
        // 设置HTML内容
        html.setContent(content, "text/html; charset=utf-8");

        // MiniMultipart容器 添加 html内容
        mainPart.addBodyPart(html);
    }
}
