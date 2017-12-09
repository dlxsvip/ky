package com.ky.logic.utils.email;

import com.ky.logic.model.info.MailInfo;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * Created by yl on 2017/8/25.
 */
public class SpringJavaEmailUtil {


    /**
     * 25 端口发送html邮件
     *
     * @param mailInfo 邮件信息
     * @throws Exception
     */
    public static void sendHtmlMailBy25(MailInfo mailInfo) throws Exception {
        JavaMailSenderImpl email = new JavaMailSenderImpl();

        // 发送邮箱的邮件服务器
        email.setHost(mailInfo.getSmtpServer());

        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = email.createMimeMessage();
        // 为防止乱码，添加编码集设置
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, "UTF-8");

        // 发送方邮箱
        messageHelper.setFrom(mailInfo.getSenderAddress(), mailInfo.getSenderNick());
        // 接收方邮箱
        to(mailInfo, messageHelper);

        //设置标题
        messageHelper.setSubject(mailInfo.getSubject());
        // true 表示启动HTML格式的邮件
        messageHelper.setText(mailInfo.getContent(), true);

        Properties prop = new Properties();
        // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.auth", "true");
        // 超时时间
        prop.put("mail.smtp.timeout", "25000");

        // 添加验证
        Session session = Session.getDefaultInstance(prop, new MyAuthenticator(mailInfo.getSenderAddress(), mailInfo.getSenderPassword()));
        email.setSession(session);

        // 发送邮件
        email.send(mailMessage);
    }


    // 收件人
    private static void to(MailInfo mailInfo, MimeMessageHelper messageHelper) throws Exception {
        List<String> toAddress = mailInfo.getToAddress();
        if (null != toAddress && toAddress.size() > 0) {
            for (int i = 0; i < toAddress.size(); i++) {
                messageHelper.setTo(toAddress.get(i));
            }
        }
    }





}
