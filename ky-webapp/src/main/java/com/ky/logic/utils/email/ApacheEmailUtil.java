package com.ky.logic.utils.email;

import com.ky.logic.model.info.MailInfo;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import java.util.List;

/**
 * apache.commons.mail
 *
 * Created by yl on 2017/8/25.
 */
public class ApacheEmailUtil {

    /**
     * 25 端口发送简单邮件
     *
     * @param mailInfo 邮件信息
     * @throws Exception
     */
    public static void sendSimpleMailBy25(MailInfo mailInfo) throws Exception {
        SimpleEmail email = new SimpleEmail();

        // 发送邮箱的邮件服务器
        email.setHostName(mailInfo.getSmtpServer());
        // 登陆邮件服务器的用户名和密码
        email.setAuthentication(mailInfo.getSenderAddress(), mailInfo.getSenderPassword());
        email.setSSLOnConnect(true);

        // 接收人地址
        to(mailInfo, email);
        // 抄送方
        cc(mailInfo, email);
        // 密送方
        bcc(mailInfo, email);

        // 设置发送人信息
        email.setFrom(mailInfo.getSenderAddress(), mailInfo.getSenderNick());
        // 设置标题
        email.setSubject(mailInfo.getSubject());
        // 填写邮件内容
        email.setMsg(mailInfo.getContent());

        // 发送邮件
        email.send();
    }


    /**
     * 25 端口发送带附件的邮件
     *
     * @param mailInfo 邮件信息
     * @throws Exception
     */
    public static void sendAttachmentBy25(MailInfo mailInfo) throws Exception {
        HtmlEmail email = new HtmlEmail();

        email.setHostName(mailInfo.getSmtpServer());
        email.setFrom(mailInfo.getSenderAddress(), mailInfo.getSenderNick());
        email.setAuthentication(mailInfo.getSenderAddress(), mailInfo.getSenderPassword());
        email.setCharset("UTF-8");
        email.setSubject(mailInfo.getSubject());
        email.setHtmlMsg(mailInfo.getContent());

        // 添加附件
        att(mailInfo, email);

        // 收件人
        to(mailInfo, email);
        // 抄送人
        cc(mailInfo, email);
        // 密送人
        bcc(mailInfo, email);

        // 发送邮件
        email.send();
    }



    // 收件人
    private static void to(MailInfo mailInfo, Email email) throws Exception {
        List<String> toAddress = mailInfo.getToAddress();
        if (null != toAddress && toAddress.size() > 0) {
            for (int i = 0; i < toAddress.size(); i++) {
                email.addTo(toAddress.get(i));
            }
        }
    }

    // 抄送方
    private static void cc(MailInfo mailInfo, Email email) throws Exception {
        List<String> ccAddress = mailInfo.getCcAddress();
        if (null != ccAddress && ccAddress.size() > 0) {
            for (int i = 0; i < ccAddress.size(); i++) {
                email.addCc(ccAddress.get(i));
            }
        }
    }

    // 密送方
    private static void bcc(MailInfo mailInfo, Email email) throws Exception {
        List<String> bccAddress = mailInfo.getBccAddress();
        if (null != bccAddress && bccAddress.size() > 0) {
            for (int i = 0; i < bccAddress.size(); i++) {
                email.addBcc(bccAddress.get(i));
            }
        }
    }

    // 添加附件
    private static void att(MailInfo mailInfo, HtmlEmail email) throws Exception {
        List<EmailAttachment> attachments = mailInfo.getAttachments();
        if (null != attachments && attachments.size() > 0) {
            for (int i = 0; i < attachments.size(); i++) {
                email.attach(attachments.get(i));
            }
        }
    }

}
